package com.sparta.cloneproject.service;

import com.sparta.cloneproject.dto.requestDto.ArticleRequestDto;
import com.sparta.cloneproject.dto.responseDto.ArticleResponseDto;
import com.sparta.cloneproject.model.Article;
import com.sparta.cloneproject.model.DeletedUrlPath;
import com.sparta.cloneproject.repository.ArticleRepository;
import com.sparta.cloneproject.repository.DeletedUrlPathRepository;
import com.sparta.cloneproject.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
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

    /**
     * 전체 게시글 조회
     *
     * @return
     */
    public List<ArticleResponseDto> getArticleList() {
        List<Article> articleList = articleRepository.findAll();
        List<ArticleResponseDto> articleResponseDto = new ArrayList<>();
        responseAllArticle(articleList, articleResponseDto);
        return articleResponseDto;
    }

    /**
     * 상세 게시글 조회
     */
    public ArticleResponseDto getDetail(Long id) {
        final Optional<Article> article = articleRepository.findById(id);
        ArticleResponseDto articleResponseDto = responseDetail(article);
        return articleResponseDto;
    }

    /**
     * 게시글 작성
     */
    public Article createArticle(ArticleRequestDto requestDto, MultipartFile multipartFile) throws IOException {
        if (multipartFile != null) {
            String pathUrl = s3Uploader.upload(multipartFile);
            String username = "아이디";
            Article article = getArticle(requestDto, pathUrl, username);
            articleRepository.save(article);
            return article;
        }
        String username = "아이디";
        Article article = getArticleNotImage(requestDto, username);
        articleRepository.save(article);
        return article;
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public Article updateArticle(Long id, ArticleRequestDto requestDto) {
        Optional<Article> findArticle = articleRepository.findById(id);
        findArticle.get().update(requestDto);
        return findArticle.get();
    }


    /**
     * 게시글 삭제
     */
    public boolean delete(Long id) {
        Optional<Article> findArticle = articleRepository.findById(id);

        // S3버켓에있는 이미지 삭제 관련
        DeletedUrlPath deletedUrlPath = new DeletedUrlPath();
        deletedUrlPath.setDeletedUrlPath(findArticle.get().getImg());
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


    private void responseAllArticle(List<Article> articleList, List<ArticleResponseDto> articleResponseDto) {
        for (Article article : articleList) {
            ArticleResponseDto result = ArticleResponseDto.builder()
                    .id(article.getId())
                    .title(article.getTitle())
                    .img(article.getImg())
                    .price(article.getPrice())
                    .region(article.getRegion())
                    .commentCnt(article.getCommentList().size()) // 게시글이 가지고있는 댓글리스트의 size() = 이게 어차피 댓글갯수니까
                    .heartCnt(article.getHeartList().size())
                    .build();

            articleResponseDto.add(result);
        }
    }

    private ArticleResponseDto responseDetail(Optional<Article> article) {
        ArticleResponseDto articleResponseDto = ArticleResponseDto.builder()
                .title(article.get().getTitle())
                .username(article.get().getUsername())
                .category(article.get().getCategory())
                .region(article.get().getRegion())
                .createAt(article.get().getCreateAt())
                .img(article.get().getImg())
                .price(article.get().getPrice())
                .build();
        return articleResponseDto;
    }

    private Article getArticle(ArticleRequestDto requestDto, String pathUrl, String username) {
        Article article = Article.builder()
                .content(requestDto.getContent())
                .price(requestDto.getPrice())
                .category(requestDto.getCategory())
                .region(requestDto.getRegion())
                .img(pathUrl)
                .username(username)
                .build();
        return article;
    }

    private Article getArticleNotImage(ArticleRequestDto requestDto, String username) {
        Article article = Article.builder()
                .content(requestDto.getContent())
                .price(requestDto.getPrice())
                .category(requestDto.getCategory())
                .region(requestDto.getRegion())
                .username(username)
                .build();
        return article;
    }

}