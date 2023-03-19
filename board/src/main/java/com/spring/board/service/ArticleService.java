package com.spring.board.service;

import com.spring.board.domain.Article;
import com.spring.board.domain.ArticleComment;
import com.spring.board.domain.constant.SearchType;
import com.spring.board.dto.ArticleCommentDto;
import com.spring.board.dto.ArticleDto;
import com.spring.board.dto.ArticleWithCommentsDto;
import com.spring.board.dto.response.ArticleCommentResponse;
import com.spring.board.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Slf4j
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
        if (!StringUtils.hasText(searchKeyword)) {
            return articleRepository
                    .findAll(pageable)
                    .map(ArticleDto::from);   // article -> ArticleDto
        }
        return switch (searchType) {
            case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable).map(ArticleDto::from);
            case CONTENT -> articleRepository.findByContentContaining(searchKeyword, pageable).map(ArticleDto::from);
            case ID -> articleRepository.findByUserAccount_UserIdContaining(searchKeyword, pageable).map(ArticleDto::from);
            case NICKNAME -> articleRepository.findByUserAccount_NicknameContaining(searchKeyword, pageable).map(ArticleDto::from);
            case HASHTAG -> articleRepository.findByHashtag("#" + searchKeyword, pageable).map(ArticleDto::from);
        };
    }


    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticleWithComments(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleWithCommentsDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
    }

    @Transactional(readOnly = true)
    public ArticleDto getArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
    }

    public void saveArticle(ArticleDto dto) {
        articleRepository.save(dto.toEntity());
    }

    // 클래스 레벨 트랜잭션 설정으로 인해, 메서드 레벨 트랜잭션 설정은 필요 없음
    // 메서드가 종료될때 영속성컨텍스트는 변경을 감지하고 자동으로 flush 됨
    public void updateArticle(ArticleDto dto) {
        try {
            var article = articleRepository.getReferenceById(dto.id());
            if (StringUtils.hasText(dto.title())) article.updateTitle(dto.title());
            if (StringUtils.hasText(dto.content())) article.updateContent(dto.content());
            if (StringUtils.hasText(dto.hashtag())) article.updateHashTag(dto.hashtag());

        } catch (EntityNotFoundException e) {
            log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습나다 - dto: {}", dto);
        }
    }

    public void deleteArticle(long articleId) {
        articleRepository.deleteById(articleId);
    }

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticlesViaHashtag(String hashtag, Pageable pageable) {
        if (!StringUtils.hasText(hashtag)) {
            return Page.empty(pageable);
        }
        return articleRepository
                .findByHashtag(hashtag, pageable)
                .map(ArticleDto::from);
    }

    public List<String> getHashtags() {
        return articleRepository.findAllDistinctHashtags();
    }
}
