package com.spring.board.service;

import com.spring.board.domain.constant.SearchType;
import com.spring.board.dto.ArticleDto;
import com.spring.board.dto.ArticleUpdateDto;
import com.spring.board.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType title, String search_keyword) {
        return Page.empty();
    }

    @Transactional(readOnly = true)
    public ArticleDto searchArticles(long articleId) {
        return null;
    }

    public void saveArticle(ArticleDto dto) {
//        articleRepository.save(dto.toEntity());
    }

    public void updateArticle(long articleId, ArticleUpdateDto dto) {

    }

    public void deleteArticle(long articleId) {

    }
}
