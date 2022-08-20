package com.sparta.cloneproject.dto.responseDto;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class ArticleResponseDto {

    /**
     * {
     * ”id”: ”게시글번호”,
     * “img”: ”이미지URL”,
     * “title”: ”제목”,
     * “price”: ”가격”,
     * “region”: ”지역”,
     * “heartCnt”: ”좋아요(찜)갯수”,
     * “commentCnt”: ”댓글갯수”
     * “username”: ”아이디”,
     * “category”: ”뷰티용품”,
     * “createAt”: ”3분전”
     * }
     */
    private long id;
    private String img;
    private String title;
    private long price;
    private String region;
    private long heartCnt;
    private long commentCnt;
    private String username;
    private Timestamp createAt;
    private String category;

    @Builder
    public ArticleResponseDto(long id, String img, String title, Long price, String region, long heartCnt, long commentCnt, String username, Timestamp createAt, String category) {
        this.id = id;
        this.img = img;
        this.title = title;
        this.price = price;
        this.region = region;
        this.heartCnt = heartCnt;
        this.commentCnt = commentCnt;
        this.username = username;
        this.createAt = createAt;
        this.category = category;
    }
}
