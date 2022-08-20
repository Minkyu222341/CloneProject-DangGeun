package com.sparta.cloneproject.service;

import com.sparta.cloneproject.model.Article;
import com.sparta.cloneproject.model.Heart;
import com.sparta.cloneproject.repository.ArticleRepository;
import com.sparta.cloneproject.repository.HeartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HeartService {
    private final HeartRepository heartRepository;
    private final ArticleRepository articleRepository;
    String username = "임시사용자";

/**
 * 게시글 찜하기 - 토글형식
 * */
    public boolean addLike(Long id) {
        Optional<Article> article = articleRepository.findById(id);
        if (heartRepository.existsByArticleAndUsername(article.get(), username)) {
            Optional<Heart> deletedHeart = heartRepository.findByUsernameAndAndArticle(username,article.get());
            article.get().deleteHeart(deletedHeart.get());
            heartRepository.delete(deletedHeart.get());
            return false;
        }
        Heart heart = getHeart(article);
        article.get().addHeart(heart);
        heartRepository.save(heart);
        return true;
    }

    private Heart getHeart(Optional<Article> article) {
        Heart heart = Heart.builder()
                .username(username)
                .article(article.get())
                .build();
        return heart;
    }
}
