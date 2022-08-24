package com.sparta.cloneproject.dto.responseDto;

import com.sparta.cloneproject.model.Img;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class SearchResponseDto {
    private Long id;

    private String title;
    private Long price;
    private String region;
    private String category;

    private String createAt;
    private Boolean isLike;

    private List<Img> imgList;

    private long heartCnt;
    private long commentCnt;

    @Builder
    public SearchResponseDto(Long id, String title, Long price, String region, String category, String createAt, Boolean isLike, List<Img> imgList, long heartCnt, long commentCnt) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.region = region;
        this.category = category;
        this.createAt = createAt;
        this.isLike = isLike;
        this.imgList = imgList;
        this.heartCnt = heartCnt;
        this.commentCnt = commentCnt;
    }
}
