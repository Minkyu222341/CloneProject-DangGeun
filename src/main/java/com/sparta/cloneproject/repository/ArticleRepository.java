package com.sparta.cloneproject.repository;

import com.sparta.cloneproject.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article,Long> {
}
