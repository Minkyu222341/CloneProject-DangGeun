package com.sparta.cloneproject.dto.responseDto;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.cloneproject.model.Img;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
public class SearchResponseDto {
    private Long id;

    private String title;
    private Long price;
    private String region;
    private String category;

    private Timestamp createAt;
    private Boolean isLike;

    private List<Img> imgList;

    @QueryProjection

    public SearchResponseDto(Long id,  String title, Long price, String region, String category, Timestamp createAt, Boolean isLike,List<Img> imgList) {
        this.id = id;

        this.title = title;
        this.price = price;
        this.region = region;
        this.category = category;
        this.createAt = createAt;
        this.isLike = isLike;
        this.imgList = imgList;
    }
}
