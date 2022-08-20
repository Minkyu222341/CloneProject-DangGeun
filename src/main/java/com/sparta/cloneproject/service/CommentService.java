package com.sparta.cloneproject.service;

import com.sparta.cloneproject.dto.requestDto.CommentRequestDto;
import com.sparta.cloneproject.dto.responseDto.CommentResponseDto;
import com.sparta.cloneproject.model.Article;
import com.sparta.cloneproject.model.Comment;
import com.sparta.cloneproject.repository.ArticleRepository;
import com.sparta.cloneproject.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
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
    String USERNAME = "임시 아이디";

    /**
     * 게시글에 해당하는 댓글 리스트 호출 , 페이지 전체 리로드를 막기 위해서 따로 호출
     */
    public List<CommentResponseDto> getComment(Long id) {
        Optional<Article> article = articleRepository.findById(id);
        final List<Comment> commentList = commentRepository.findByArticle(article.get());
        List<CommentResponseDto> responseDtos = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentResponseDto buildComment = CommentResponseDto.builder()
                    .content(comment.getContent())
                    .id(comment.getId())
                    .username(USERNAME)
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
        Comment comment = getComment(commentRequestDto, article);
        article.get().addComment(comment);
        return commentRepository.save(comment);
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public Comment updateComment(Long id, CommentRequestDto commentRequestDto) {
        Optional<Comment> findComment = commentRepository.findById(id);
        findComment.get().update(commentRequestDto);
        return findComment.get();
    }

    /**
     * 댓글 삭제
     */
    public boolean deleteComment(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        Article article = comment.get().getArticle();
        article.remove(comment);
        commentRepository.deleteById(id);
        return true;
    }


    private Comment getComment(CommentRequestDto commentRequestDto, Optional<Article> article) {
        Comment comment = Comment.builder()
                .article(article.get())
                .content(commentRequestDto.getContent())
                .username(USERNAME)
                .build();
        return comment;
    }
}
