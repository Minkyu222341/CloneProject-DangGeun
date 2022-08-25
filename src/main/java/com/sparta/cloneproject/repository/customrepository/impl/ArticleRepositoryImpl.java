package com.sparta.cloneproject.repository.customrepository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.cloneproject.dto.responseDto.ArticleResponseDto;
import com.sparta.cloneproject.dto.responseDto.SearchResponseDto;
import com.sparta.cloneproject.model.Article;
import com.sparta.cloneproject.repository.customrepository.ArticleRepositoryCustom;
import com.sparta.cloneproject.util.TimeCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

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
                    .userId(article.getUserId())
                    .id(article.getId())
                    .title(article.getTitle())
                    .price(article.getPrice())
                    .region(article.getRegion())
                    .category(article.getCategory())
                    .createAt(timeCustom.customTime(article.getCreateAt()))
                    .isLike(article.getIsLike())
                    .imgList(article.getImgList())
                    .heartCnt(article.getHeartList().size())
                    .commentCnt(article.getCommentList().size())
                    .build());
        }

        return dtoList;
    }

    /**
     * 무한스크롤 , 카테고리 검색
     */
    @Override
    public Slice<ArticleResponseDto> searchScroll(Pageable pageable, String category, String region) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QueryResults<Article> result = queryFactory
                .selectFrom(article)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(article.id.desc())
                .where(categoryEq(category),
                        regionEq(region))
                .fetchResults();

        List<ArticleResponseDto> dto = new ArrayList<>();

        for (Article article : result.getResults()) {
            dto.add(ArticleResponseDto.builder()
                    .userId(article.getUserId())
                    .id(article.getId())
                    .title(article.getTitle())
                    .price(article.getPrice())
                    .region(article.getRegion())
                    .category(article.getCategory())
                    .createAt(timeCustom.customTime(article.getCreateAt()))
                    .isLike(article.getIsLike())
                    .img(article.getImgList())
                    .heartCnt(article.getHeartList().size())
                    .commentCnt(article.getCommentList().size())
                    .build());
        }
        boolean hasNext = false;
        if (dto.size() > pageable.getPageSize()) {
            dto.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(dto, pageable, hasNext);
    }


    //    private BooleanExpression regionEq(String region) {
//        return region != null ? article.region.eq(region) : null;
//    }
    private BooleanExpression regionEq(String region) {
        if (region == null) {
            return null;
        } else if (region == "") {
            return null;
        }
        return article.region.like(region);
    }

    private BooleanExpression categoryEq(String category) {
        if (category == null) {
            return null;
        } else if (category == "") {
            return null;
        }
        return article.category.like(category);
    }

}
