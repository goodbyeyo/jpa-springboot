package com.spring.board.domain.constant;

import lombok.Getter;

public enum SearchType {
    TITLE("제목"),
    CONTENT("본문"),
    ID("유저ID"),
    HASHTAG("해시태그"),
    NICKNAME("닉네임");

    @Getter
    public final String description;

    SearchType(String description) {
        this.description = description;
    }
}
