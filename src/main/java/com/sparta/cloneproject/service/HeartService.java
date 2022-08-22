package com.sparta.cloneproject.service;

import com.sparta.cloneproject.model.Article;
import com.sparta.cloneproject.model.Heart;
import com.sparta.cloneproject.model.Member;
import com.sparta.cloneproject.repository.ArticleRepository;
import com.sparta.cloneproject.repository.HeartRepository;
import com.sparta.cloneproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HeartService {
    private final HeartRepository heartRepository;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    public Optional<Member> getLoginMember() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Member> findLoginMember = memberRepository.findByUsername(userId);
        System.out.println(findLoginMember.get().getId());
        return findLoginMember;
    }

/**
 * 게시글 찜하기 - 토글형식
 * */
    public boolean addLike(Long id) {
        Optional<Article> article = articleRepository.findById(id);
        if (heartRepository.existsByArticleAndUserId(article.get(), getLoginMember().get().getId())) {
            Optional<Heart> deletedHeart = heartRepository.findByUserIdAndAndArticle(getLoginMember().get().getId(),article.get());
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
                .userId(getLoginMember().get().getId())
                .article(article.get())
                .build();
        return heart;
    }
}
