package com.sparta.cloneproject.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Img {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imgUrl;

    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "article_id")
    @JsonBackReference
    private Article article;

    @Builder
    public Img(Long id, String imgUrl, Article article,String fileName) {
        this.id = id;
        this.imgUrl = imgUrl;
        this.article = article;
        this.fileName = fileName;
    }
}
