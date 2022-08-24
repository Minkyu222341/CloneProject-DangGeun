package com.sparta.cloneproject.repository.customrepository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.cloneproject.dto.responseDto.SearchResponseDto;
import com.sparta.cloneproject.model.Article;
import com.sparta.cloneproject.repository.customrepository.ArticleRepositoryCustom;
import com.sparta.cloneproject.util.TimeCustom;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import static com.sparta.cloneproject.model.QArticle.article;

@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

    @PersistenceContext
    EntityManager em;
    private final TimeCustom timeCustom;

    /**
     * 카테고리 검색기능
     */
    @Override
    public List<SearchResponseDto> search(String region, String category) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<Article> fetch = queryFactory
                .selectFrom(article)
                .where(categoryEq(category),
                        regionEq(region))
                .fetch();

        List<SearchResponseDto> dtoList = new ArrayList<>();

        for (Article article : fetch) {
            dtoList.add(SearchResponseDto.builder()
                    .id(article.getId())
                    .title(article.getTitle())
                    .price(article.getPrice())
                    .region(article.getRegion())
                    .category(article.getCategory())
                    .createAt(timeCustom.customTime(article.getCreateAt()))
                    .isLike(article.getIsLike())
                    .imgList(article.getImgList())
                    .heartCnt(article.getCommentList().size())
                    .commentCnt(article.getHeartList().size())
                    .build());
        }

        return dtoList;
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
