package com.spring.board.dto.response;

import com.spring.board.domain.Article;
import com.spring.board.domain.ArticleComment;
import com.spring.board.dto.ArticleCommentDto;
import com.spring.board.dto.UserAccountDto;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public record ArticleCommentResponse(
        Long id,
        String content,
        String userId,
        String email,
        String nickname,
        LocalDateTime createdAt
) implements Serializable {

    public static ArticleCommentResponse of(Long id, String content, String userId, String email, String nickname, LocalDateTime createdAt) {
        return new ArticleCommentResponse(id, content, userId, email, nickname, createdAt);
    }

    public static ArticleCommentResponse from(ArticleCommentDto dto) {
        String nickname = dto.userAccountDto().nickname();
        if (!StringUtils.hasText(nickname)) {
            nickname = dto.userAccountDto().userId();
        }
        return new ArticleCommentResponse(
                dto.id(),
                dto.content(),
                dto.userAccountDto().userId(),
                dto.userAccountDto().email(),
                nickname,
                dto.createdAt()
        );
    }
}
