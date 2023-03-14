package com.spring.board.repository;

import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.spring.board.domain.Article;
import com.spring.board.domain.QArticle;
import com.spring.board.dto.ArticleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        QuerydslPredicateExecutor<Article>, // Querydsl 설정 + 기본 검색 기능 추가
        QuerydslBinderCustomizer<QArticle> {

    // 검색에 대한 세부적인 규칙 재구성
    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {
        bindings.excludeUnlistedProperties(true);   // 검색에 포함되지 않는 프로퍼티는 검색에서 제외
        bindings.including(root.title, root.content, root.hashtag, root.createdAt, root.createdBy); // 검색에 포함할 프로퍼티 지정
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase); // like '%$ {value} %'
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
//        bindings.bind(root.title).first((path, value) -> path.eq(value));   // StringExpression::eq
//        bindings.bind(root.title).first(StringExpression::likeIgnoreCase);  // like '${value} %
    }

    Page<ArticleDto> findByTitle(String title, Pageable pageable);
}
