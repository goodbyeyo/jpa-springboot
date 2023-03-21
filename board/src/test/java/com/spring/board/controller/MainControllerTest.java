package com.spring.board.controller;

import com.spring.board.config.SecurityConfig;
import com.spring.board.config.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 루트 컨트롤러")
@Import(TestSecurityConfig.class)
@WebMvcTest(MainController.class)
class MainControllerTest {

    private final MockMvc mvc;

    public MainControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 루트 페이지 -> 게시글 리스트 (게시판) 페이지 Redirection")
    @Test
    void givenRootPath_whenRequestRootPage_thenRedirectsToArticlesPage() throws Exception {
        // given

        // when & then
        mvc.perform(get("/")).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("/articles"));

        // When & Then (forwarding) 안되는 이유 찾아야 함...
//        mvc.perform(get("/"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("forward:/articles"))
//                .andExpect(forwardedUrl("/articles"));


    }

}