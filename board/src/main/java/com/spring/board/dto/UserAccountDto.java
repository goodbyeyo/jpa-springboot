package com.spring.board.dto;

import com.spring.board.domain.UserAccount;

import java.time.LocalDateTime;

/**
 * A DTO for the {@link com.spring.board.domain.UserAccount} entity
 */
public record UserAccountDto(
        //Long id,
        String userId,
        String userPassword,
        String email,
        String nickname,
        String memo,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {
    public static UserAccountDto of(
                                 // Long id,
                                 String userId,
                                 String userPassword,
                                 String email,
                                 String nickname,
                                 String memo,
                                 LocalDateTime createdAt,
                                 String createdBy,
                                 LocalDateTime modifiedAt,
                                 String modifiedBy
                                    ) {
        return new UserAccountDto(/*id,*/ userId, userPassword, email, nickname, memo, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static UserAccountDto from(UserAccount userAccount) {
        return new UserAccountDto(
                /*userAccount.getId(),*/
                userAccount.getUserId(),
                userAccount.getUserPassword(),
                userAccount.getEmail(),
                userAccount.getNickname(),
                userAccount.getMemo(),
                userAccount.getCreatedAt(),
                userAccount.getCreatedBy(),
                userAccount.getModifiedAt(),
                userAccount.getModifiedBy()
        );
    }

    public UserAccount toEntity() {
        return null;
    }
}