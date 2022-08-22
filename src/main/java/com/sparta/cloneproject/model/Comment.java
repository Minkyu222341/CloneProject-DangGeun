package com.sparta.cloneproject.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sparta.cloneproject.dto.requestDto.CommentRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String content;
    private String nickname;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "articleId")
    private Article article;

    @Builder
    public Comment(Long id, Long userId, String content, String nickname, Article article) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.nickname = nickname;
        this.article = article;
    }


    public void update(CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContent();
    }
}
