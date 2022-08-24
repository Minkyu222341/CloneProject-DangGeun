package com.sparta.cloneproject.repository.customrepository;

import com.sparta.cloneproject.dto.responseDto.SearchResponseDto;
import com.sparta.cloneproject.model.Article;

import java.util.List;

public interface ArticleRepositoryCustom {

    List<SearchResponseDto> search(String region,String category);

    List<Article> searchAll(String region,String category);
}
