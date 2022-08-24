package com.sparta.cloneproject.service;

import com.sparta.cloneproject.dto.requestDto.ArticleRequestDto;
import com.sparta.cloneproject.dto.responseDto.ArticleResponseDto;
import com.sparta.cloneproject.dto.responseDto.SearchResponseDto;
import com.sparta.cloneproject.model.Article;
import com.sparta.cloneproject.model.DeletedUrlPath;
import com.sparta.cloneproject.model.Img;
import com.sparta.cloneproject.model.Member;
import com.sparta.cloneproject.repository.*;
import com.sparta.cloneproject.s3.S3Dto;
import com.sparta.cloneproject.s3.S3Uploader;
import com.sparta.cloneproject.util.TimeCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final S3Uploader s3Uploader;
    private final DeletedUrlPathRepository deletedUrlPathRepository;
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;
    private final HeartRepository heartRepository;
    private final TimeCustom timeCustom;
    public Optional<Member> getLoginMember() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberRepository.findByUsername(userId);
    }


    /**
     * 전체 게시글 조회
     *
     * @return
     */
    public List<ArticleResponseDto> getArticleList() {
        List<Article> articleList = articleRepository.findAll();

        List<ArticleResponseDto> articleResponseDto = new ArrayList<>();
        for (Article article : articleList) {
            if(getLoginMember().isPresent()) {
                if (heartRepository.existsByArticleAndUserId(article,getLoginMember().get().getId())) {
                    article.setIsLike(true);
                } else {
                    article.setIsLike(false);
                }
            }
            ArticleResponseDto result = ArticleResponseDto.builder()
                    .id(article.getId())
                    .title(article.getTitle())
                    .img(article.getImgList())
                    .price(article.getPrice())
                    .region(article.getRegion())
                    .commentCnt(article.getCommentList().size())
                    .heartCnt(article.getHeartList().size())
                    .isLike(article.getIsLike())
                    .build();

            articleResponseDto.add(result);
        }
        return articleResponseDto;
    }

    /**
     * 상세 게시글 조회
     */
    public ArticleResponseDto getDetail(Long id) {
        Optional<Article> article = articleRepository.findById(id);
        Optional<Member> author = memberRepository.findById(article.get().getUserId());
        if(getLoginMember().isPresent()) {
            if (heartRepository.existsByArticleAndUserId(article.get(), getLoginMember().get().getId())) {
                article.get().setIsLike(true);
            } else {
                article.get().setIsLike(false);
            }
        }

        ArticleResponseDto articleResponseDto = ArticleResponseDto.builder()
                .title(article.get().getTitle())
                .nickname(article.get().getNickname())
                .category(article.get().getCategory())
                .region(article.get().getRegion())
                .createAt(timeCustom.customTime(article.get().getCreateAt()))
                .img(article.get().getImgList())
                .price(article.get().getPrice())
                .isLike(article.get().getIsLike())
                .content(article.get().getContent())
                .commentCnt(article.get().getCommentList().size())
                .heartCnt(article.get().getHeartList().size())
                .username(author.get().getUsername())
                .build();
        return articleResponseDto;
    }

    /**
     * 게시글 작성
     */
    public Article createArticle(ArticleRequestDto requestDto, List<MultipartFile> multipartFile) throws IOException {
        List<Img> imgList = new ArrayList<>();
        if (multipartFile != null) {
            Long userId = getLoginMember().get().getId();
            String nickname = getLoginMember().get().getNickname();
            Article article = Article.builder()
                    .title(requestDto.getTitle())
                    .content(requestDto.getContent())
                    .price(requestDto.getPrice())
                    .category(requestDto.getCategory())
                    .region(requestDto.getRegion())
                    .userId(userId)
                    .nickname(nickname)
                    .isLike(false)
                    .build();

            for (MultipartFile file : multipartFile) {
                S3Dto upload = s3Uploader.upload(file);
                Img findImage = Img.builder()
                        .imgUrl(upload.getUploadImageUrl())
                        .fileName(upload.getFileName())
                        .article(article)
                        .build();
                imgList.add(findImage);
                imageRepository.save(findImage);
            }

            articleRepository.save(article);
            return article;
        }
        Long userId = getLoginMember().get().getId();
        String nickname = getLoginMember().get().getNickname();
        Article article = Article.builder()
                .content(requestDto.getContent())
                .price(requestDto.getPrice())
                .category(requestDto.getCategory())
                .region(requestDto.getRegion())
                .userId(userId)
                .nickname(nickname)
                .isLike(false)
                .build();
        articleRepository.save(article);
        return article;
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public Article updateArticle(Long id, ArticleRequestDto requestDto) {
        Optional<Article> findArticle = articleRepository.findById(id);
        if(!findArticle.get().getUserId().equals(getLoginMember().get().getId())){
            throw new IllegalArgumentException("작성자만 수정 할 수 있습니다.");
        }
        findArticle.get().update(requestDto);
        return findArticle.get();
    }


    /**
     * 게시글 삭제
     */
    public boolean delete(Long id) {
        Optional<Article> findArticle = articleRepository.findById(id);

        if(!findArticle.get().getUserId().equals(getLoginMember().get().getId())){
            throw new IllegalArgumentException("작성자만 삭제 할 수 있습니다.");
        }
        // S3버켓에있는 이미지 삭제 관련
        DeletedUrlPath deletedUrlPath = new DeletedUrlPath();
//        deletedUrlPath.setDeletedUrlPath(findArticle.get().getImgList());
        deletedUrlPathRepository.save(deletedUrlPath);

        articleRepository.deleteById(id);
        return true;
    }

    /**
     * S3에 있는 이미지 삭제
     */
    public void removeS3Image() {
        List<DeletedUrlPath> deletedUrlPathList = deletedUrlPathRepository.findAll();
        for (DeletedUrlPath deletedUrlPath : deletedUrlPathList) {
            s3Uploader.remove(deletedUrlPath.getDeletedUrlPath());
        }
        deletedUrlPathRepository.deleteAll();
    }

    /**
     * 카데고리 조회
     */
    public List<SearchResponseDto> searchCategory(String region,String category) {
        System.out.println("지역 : "+region);
        System.out.println("카테고리 : "+category);
        return articleRepository.search(region,category);
    }
}
