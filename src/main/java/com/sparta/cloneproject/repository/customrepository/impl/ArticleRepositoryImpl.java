package com.sparta.cloneproject.repository.customrepository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.cloneproject.dto.responseDto.QSearchResponseDto;
import com.sparta.cloneproject.dto.responseDto.SearchResponseDto;
import com.sparta.cloneproject.model.Article;
import com.sparta.cloneproject.repository.customrepository.ArticleRepositoryCustom;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.sparta.cloneproject.model.QArticle.article;

@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

    @PersistenceContext
    EntityManager em;
//    JPAQueryFactory queryFactory ;

    @Override
    public List<SearchResponseDto> search(String region, String category) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        List<SearchResponseDto> fetch = queryFactory
                .select(new QSearchResponseDto(
                        article.id,
                        article.title,
                        article.price,
                        article.region,
                        article.category,
                        article.createAt,
                        article.isLike,
                        article.imgList))
                .from(article)
                .where(categoryEq(category),
                        regionEq(region))
                .fetch();


        return fetch;
    }

    @Override
    public List<Article> searchAll(String region, String category) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<Article> fetch = queryFactory
                .selectFrom(article)
                .where(categoryEq(category),
                        regionEq(region))
                .fetch();
        return fetch;
    }


    private BooleanExpression regionEq(String region) {
        System.out.println("지역비교");
        return region != null ? article.region.eq(region) : null;
    }

    private BooleanExpression categoryEq(String category) {
        System.out.println("카테고리비교");
        return category != null ? article.category.eq(category) : null;
    }

}
