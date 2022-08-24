package com.sparta.cloneproject.repository.customrepository;

import com.sparta.cloneproject.dto.responseDto.ArticleResponseDto;
import com.sparta.cloneproject.dto.responseDto.SearchResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ArticleRepositoryCustom {

    List<SearchResponseDto> search(String region,String category);

    Slice<ArticleResponseDto> searchScroll(Pageable pageable,String category,String region);

}
