//package com.sparta.cloneproject.repository.customrepository.impl;
//
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import com.sparta.cloneproject.model.Article;
//import com.sparta.cloneproject.model.QArticle;
//import com.sparta.cloneproject.repository.customrepository.ArticleCustomRepository;
//import lombok.RequiredArgsConstructor;
//
//import java.util.List;
//
//import static com.sparta.cloneproject.model.QArticle.*;
//
//@RequiredArgsConstructor
//public class ArticleCustomRepositoryImpl implements ArticleCustomRepository {
//
//    private final JPAQueryFactory queryFactory;
//
//    @Override
//    public List<Article> searchCategory(String category) {
//        queryFactory
//                .select(article.category,article.createAt,article.id,article.img,article.price,article.region,article.title)
//                .from(article)
//                .where()
//
//        return null;
//    }
//}
