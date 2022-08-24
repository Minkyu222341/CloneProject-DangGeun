package com.sparta.cloneproject.service;

import com.sparta.cloneproject.dto.requestDto.CommentRequestDto;
import com.sparta.cloneproject.dto.responseDto.CommentResponseDto;
import com.sparta.cloneproject.model.Article;
import com.sparta.cloneproject.model.Comment;
import com.sparta.cloneproject.model.Member;
import com.sparta.cloneproject.repository.ArticleRepository;
import com.sparta.cloneproject.repository.CommentRepository;
import com.sparta.cloneproject.repository.MemberRepository;
import com.sparta.cloneproject.util.TimeCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final TimeCustom timeCustom;

    public Optional<Member> getLoginMember() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Member> findLoginMember = memberRepository.findByUsername(userId);
        System.out.println(findLoginMember.get().getId());
        return findLoginMember;
    }

    /**
     * 게시글에 해당하는 댓글 리스트 호출 , 페이지 전체 리로드를 막기 위해서 따로 호출
     */
    public List<CommentResponseDto> getComment(Long id) {
        Optional<Article> article = articleRepository.findById(id);
        final List<Comment> commentList = commentRepository.findByArticleOrderByCreateAtDesc(article.get());
        List<CommentResponseDto> responseDtos = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentResponseDto buildComment = CommentResponseDto.builder()
                    .content(comment.getContent())
                    .id(comment.getId())
                    .nickname(comment.getNickname())
                    .username(comment.getUsername())
                    .creatAt(timeCustom.customTime(comment.getCreateAt()))
                    .build();
            responseDtos.add(buildComment);
        }
        return responseDtos;
    }

    /**
     * 댓글 작성
     */
    public Comment createComment(Long id, CommentRequestDto commentRequestDto) {
        Optional<Article> article = articleRepository.findById(id);
        Comment comment = createComment(commentRequestDto, article);
        article.get().addComment(comment);
        return commentRepository.save(comment);
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public Comment updateComment(Long id, CommentRequestDto commentRequestDto) {
        Optional<Comment> findComment = commentRepository.findById(id);
        if(!findComment.get().getUserId().equals(getLoginMember().get().getId())){
            throw new IllegalArgumentException("작성자만 수정 할 수 있습니다.");
        }
        findComment.get().update(commentRequestDto);
        return findComment.get();
    }

    /**
     * 댓글 삭제
     */
    public boolean deleteComment(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);

        if(!comment.get().getUserId().equals(getLoginMember().get().getId())){
            throw new IllegalArgumentException("작성자만 삭제 할 수 있습니다.");
        }

        Article article = comment.get().getArticle();
        article.remove(comment);
        commentRepository.deleteById(id);
        return true;
    }


    private Comment createComment(CommentRequestDto commentRequestDto, Optional<Article> article) {
        Comment comment = Comment.builder()
                .article(article.get())
                .content(commentRequestDto.getContent())
                .userId(getLoginMember().get().getId())
                .nickname(getLoginMember().get().getNickname())
                .username(getLoginMember().get().getUsername())
                .build();

        return comment;
    }
}
