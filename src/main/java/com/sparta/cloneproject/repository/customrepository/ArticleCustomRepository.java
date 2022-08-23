package com.sparta.cloneproject.repository.customrepository;

import com.sparta.cloneproject.model.Article;

import java.util.List;

public interface ArticleCustomRepository {

    List<Article> searchCategory(String category);
}
