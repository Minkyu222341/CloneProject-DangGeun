package com.sparta.cloneproject.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sparta.cloneproject.dto.requestDto.CommentRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

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
    private String username;
    //작성시간
    @CreationTimestamp
    private Timestamp createAt;

    @Builder
    public Comment(Long id, Long userId, String content, String nickname, Article article, String username, Timestamp createAt) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.nickname = nickname;
        this.article = article;
        this.username = username;
        this.createAt = createAt;
    }





    public void update(CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContent();
    }
}
