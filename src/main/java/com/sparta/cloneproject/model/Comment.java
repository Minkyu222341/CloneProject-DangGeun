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

    private String username;
    private String content;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "articleId")
    private Article article;

    @Builder
    public Comment(Long id, String username, String content, Article article) {
        this.id = id;
        this.username = username;
        this.content = content;
        this.article = article;
    }

    public void update(CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContent();
    }
}
