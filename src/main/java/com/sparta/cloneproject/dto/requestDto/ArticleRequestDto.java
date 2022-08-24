package com.sparta.cloneproject.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleRequestDto {
    /**
     * {
     *    “img”: ”이미지URL”,
     *     “region”: ”지역”,
     *     “title”: ”제목”,
     *     “category”: ”뷰티용품”,
     *     “price”: ”3000원”,
     *     “content”: ”내용”
     * }
     */
    private String title;
    private String content;
    private Long price;
    private String category;
    private String region;

}
