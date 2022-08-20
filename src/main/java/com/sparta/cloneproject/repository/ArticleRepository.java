package com.sparta.cloneproject.repository;

import com.sparta.cloneproject.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article,Long> {
}
