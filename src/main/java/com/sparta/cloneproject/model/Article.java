package com.sparta.cloneproject.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sparta.cloneproject.util.Timestamped;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

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
    //찜 갯수
    private Long heartCnt;
    //댓글갯수
    private Long commentCnt;
    //카테고리
    private String category;
    //지역
    private String region;
    //가격
    private Long price;

    @OneToMany(mappedBy = "article",cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Comment> commentList;
    //작성시간
    @CreationTimestamp
    private Timestamp createAt;

    @Builder
    public Article(Long id, String title, String content, String username, String img, Long heartCnt, Long commentCnt, String category, String region, Long price, List<Comment> commentList, Timestamp createAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.username = username;
        this.img = img;
        this.heartCnt = heartCnt;
        this.commentCnt = commentCnt;
        this.category = category;
        this.region = region;
        this.price = price;
        this.commentList = commentList;
        this.createAt = createAt;
    }
}
