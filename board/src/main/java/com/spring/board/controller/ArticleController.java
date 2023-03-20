package com.spring.board.controller;

import com.spring.board.domain.Article;
import com.spring.board.domain.constant.FormStatus;
import com.spring.board.domain.constant.SearchType;
import com.spring.board.dto.ArticleDto;
import com.spring.board.dto.ArticleWithCommentsDto;
import com.spring.board.dto.UserAccountDto;
import com.spring.board.dto.request.ArticleRequest;
import com.spring.board.dto.response.ArticleCommentResponse;
import com.spring.board.dto.response.ArticleResponse;
import com.spring.board.dto.response.ArticleWithCommentsResponse;
import com.spring.board.service.ArticleService;
import com.spring.board.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/articles")
@RequiredArgsConstructor
@Controller
public class ArticleController {

    private final ArticleService articleService;
    private final PaginationService paginationService;
    @GetMapping
    public String articles(
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map ) {

        var articles = articleService
                .searchArticles(searchType, searchValue, pageable)
                .map(ArticleResponse::from);

        var paginationBarNumbers = paginationService
                .getPaginationBarNumbers(pageable.getPageNumber(), articles.getTotalPages());

        map.addAttribute("articles", articles);
        map.addAttribute("paginationBarNumbers", paginationBarNumbers);
        map.addAttribute("searchTypes", SearchType.values());

        return "articles/index";
    }

    @GetMapping("/{articleId}")
    public String article(@PathVariable Long articleId, ModelMap map) {
//        ArticleDto article = articleService.getArticle(articleId);
//        map.addAttribute("article", ArticleResponse.from(article));
        var article = articleService.getArticleWithComments(articleId);
        map.addAttribute("article", ArticleWithCommentsResponse.from(article));
        map.addAttribute("articleComments", List.of());
        return "articles/detail";
    }

    @GetMapping("/search-hashtag")
    public String searchHashtag(
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 10, sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map
    ) {
        var articles= articleService.searchArticlesViaHashtag(searchValue, pageable).map(ArticleResponse::from);
        var barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), articles.getTotalPages());
        List<String> hashtags = articleService.getHashtags();

        map.addAttribute("articles", articles);
        map.addAttribute("hashtags", hashtags);
        map.addAttribute("paginationBarNumbers", barNumbers);
        map.addAttribute("searchType", SearchType.HASHTAG);

        return "articles/search-hashtag";
    }

    @GetMapping("/form")
    public String articleForm(ModelMap map) {
        map.addAttribute("formStatus", FormStatus.CREATE);

        return "articles/form";
    }

    @PostMapping ("/form")
    public String postNewArticle(ArticleRequest articleRequest) {
        // TODO: 인증 정보를 넣어줘야 한다.
        articleService.saveArticle(articleRequest.toDto(UserAccountDto.of(
                "Woo", "woo1234", "woo@mail.com", "woo", "memo")));


        return "redirect:/articles";
    }

    @GetMapping("/{articleId}/form")
    public String updateArticleForm(@PathVariable Long articleId, ModelMap map) {
        ArticleResponse article = ArticleResponse.from(articleService.getArticle(articleId));

        map.addAttribute("article", article);
        map.addAttribute("formStatus", FormStatus.UPDATE);

        return "articles/form";
    }

    @PostMapping("/{articleId}/form")
    public String updateArticle(@PathVariable Long articleId, ArticleRequest articleRequest) {
        // TODO: 인증 정보를 넣어줘야 한다.
        articleService.updateArticle(articleId, articleRequest.toDto(UserAccountDto.of(
                "woo", "password", "woo@mail.com", "Woo", "memo")));
        return "redirect:/articles/" + articleId;
    }

    @PostMapping ("/{articleId}/delete")
    public String deleteArticle(@PathVariable Long articleId) {
        // TODO: 인증 정보를 넣어줘야 한다.
        articleService.deleteArticle(articleId);

        return "redirect:/articles";
    }

}
