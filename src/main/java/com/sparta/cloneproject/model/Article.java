package com.sparta.cloneproject.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sparta.cloneproject.dto.requestDto.ArticleRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Entity
@NoArgsConstructor
@Setter
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //제목
    private String title;
    //글내용
    private String content;
    //작성자 아이디
    private Long userId;
    //작성자 닉네임
    private String nickname;
    //이미지 
//    private String img;
    @OneToMany(mappedBy = "article",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Img> imgList = new ArrayList<>();
    //카테고리
    private String category;
    //지역
    private String region;
    //가격
    private long price;

    private Boolean isLike;

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Heart> heartList = new ArrayList<>();

    @OneToMany(mappedBy = "article",cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
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
    public Article(Long id, String title, String content, Long userId, String nickname, List<Img> imgList, String category, String region, long price, List<Heart> heartList, List<Comment> commentList, Timestamp createAt,Boolean isLike) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.nickname = nickname;
        this.imgList = imgList;
        this.category = category;
        this.region = region;
        this.price = price;
        this.heartList = heartList;
        this.commentList = commentList;
        this.createAt = createAt;
        this.isLike = isLike;
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
