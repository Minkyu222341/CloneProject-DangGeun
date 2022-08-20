package com.sparta.cloneproject.repository;

import com.sparta.cloneproject.model.Article;
import com.sparta.cloneproject.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByArticle(Article article);
}
