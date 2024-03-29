package com.spring.board.service;

import com.spring.board.domain.UserAccount;
import com.spring.board.domain.constant.SearchType;
import com.spring.board.dto.ArticleDto;
import com.spring.board.dto.ArticleWithCommentsDto;
import com.spring.board.repository.ArticleRepository;
import com.spring.board.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Slf4j
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserAccountRepository userAccountRepository;

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
        UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());
        articleRepository.save(dto.toEntity(userAccount));
    }

    // 클래스 레벨 트랜잭션 설정으로 인해, 메서드 레벨 트랜잭션 설정은 필요 없음
    // 메서드가 종료될때 영속성컨텍스트는 변경을 감지하고 자동으로 flush 됨
    public void updateArticle(Long articleId, ArticleDto dto) {
        try {
            var article = articleRepository.getReferenceById(articleId);
            UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());
            if (!article.getUserAccount().equals(userAccount)) {    // 게시글 사용자와 인증된 사용자가 동일한지 체크
                throw new IllegalArgumentException("게시글 작성자만 수정할 수 있습니다.");
            }
            if (StringUtils.hasText(dto.title())) article.updateTitle(dto.title());
            if (StringUtils.hasText(dto.content())) article.updateContent(dto.content());
            if (StringUtils.hasText(dto.hashtag())) article.updateHashTag(dto.hashtag());
        } catch (EntityNotFoundException e) {
            log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습나다 - {} ", e.getLocalizedMessage());
        }
    }

    public void deleteArticle(long articleId, String userId) {
        // 게시글 작성자만 삭제 가능
        articleRepository.deleteByIdAndUserAccount_UserId(articleId, userId);
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
