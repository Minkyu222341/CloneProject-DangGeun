package com.sparta.cloneproject.dto.responseDto;

import com.sparta.cloneproject.model.Img;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

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
     * "content" : "내용"
     * }
     */
    private long id;
    private List<Img> img;
    private String title;
    private long price;
    private String region;
    private long heartCnt;
    private long commentCnt;
    private String nickname;
    private String createAt;
    private String category;
    private boolean isLike;
    private String username;
    private String content;

    @Builder
    public ArticleResponseDto(long id, List<Img> img, String title, long price, String region, long heartCnt, long commentCnt, String nickname, String createAt, String category, boolean isLike,String username,String content) {
        this.id = id;
        this.img = img;
        this.title = title;
        this.price = price;
        this.region = region;
        this.heartCnt = heartCnt;
        this.commentCnt = commentCnt;
        this.nickname = nickname;
        this.createAt = createAt;
        this.category = category;
        this.isLike = isLike;
        this.username = username;
        this.content = content;

    }
}
