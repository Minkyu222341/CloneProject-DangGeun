package com.sparta.cloneproject.repository;

import com.sparta.cloneproject.model.Article;
import com.sparta.cloneproject.model.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {

    boolean existsByArticleAndUsername(Article id, String username);

    Optional<Heart> findByUsernameAndAndArticle(String username,Article article);
}
