package com.sparta.cloneproject.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Heart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "articleId")
    private Article article;
    private String username;

    @Builder
    public Heart(Long id, Article article, String username) {
        this.id = id;
        this.article = article;
        this.username = username;
    }


}
