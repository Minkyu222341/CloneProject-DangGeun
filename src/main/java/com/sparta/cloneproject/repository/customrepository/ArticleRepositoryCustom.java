package com.sparta.cloneproject.repository.customrepository;

import com.sparta.cloneproject.dto.responseDto.SearchResponseDto;

import java.util.List;

public interface ArticleRepositoryCustom {

    List<SearchResponseDto> search(String region,String category);
    List<SearchResponseDto> searchAll(String region, String category);

}
