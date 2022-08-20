package com.sparta.cloneproject.controller;

import com.sparta.cloneproject.dto.requestDto.CommentRequestDto;
import com.sparta.cloneproject.dto.responseDto.CommentResponseDto;
import com.sparta.cloneproject.model.Comment;
import com.sparta.cloneproject.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 게시글에 달린 댓글 조회
     */
    @GetMapping("/{id}")
    public List<CommentResponseDto> getComment(@PathVariable Long id) {
        return commentService.getComment(id);
    }

    /**
     * 댓글 작성
     */
    @PostMapping("/auth/{id}")
    public Comment createComment(@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto) {
        return commentService.createComment(id, commentRequestDto);
    }

    /**
     * 댓글 수정
     */
    @PatchMapping("/auth/{id}")
    public Comment updateComment(@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto) {
        return commentService.updateComment(id, commentRequestDto);
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/auth/{id}")
    public boolean deleteComment(@PathVariable Long id) {
        return commentService.deleteComment(id);
    }
}
