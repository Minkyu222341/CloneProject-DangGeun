package com.sparta.cloneproject.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sparta.cloneproject.dto.requestDto.ArticleRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Entity
@NoArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //제목
    private String title;
    //글내용
    private String content;
    //작성자 아이디
    private String username;
    //이미지 
    private String img;
    //카테고리
    private String category;
    //지역
    private String region;
    //가격
    private long price;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Heart> heartList = new ArrayList<>();

    @OneToMany(mappedBy = "article",cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Comment> commentList = new ArrayList<>();
    //작성시간
    @CreationTimestamp
    private Timestamp createAt;


    public void update(ArticleRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.category = requestDto.getCategory();
        this.region = requestDto.getRegion();
        this.price = requestDto.getPrice();
    }


    @Builder
    public Article(Long id, String title, String content, String username, String img, String category, String region, Long price, List<Comment> commentList, Timestamp createAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.username = username;
        this.img = img;
        this.category = category;
        this.region = region;
        this.price = price;
        this.commentList = commentList;
        this.createAt = createAt;
    }

    public void addComment(Comment comment) {
        commentList.add(comment);
    }

    public void remove(Optional<Comment> comment) {
        commentList.remove(comment);
    }

    public void addHeart(Heart heart) {
        heartList.add(heart);
    }

    public void deleteHeart(Heart heart) {
        heartList.remove(heart);
    }
}