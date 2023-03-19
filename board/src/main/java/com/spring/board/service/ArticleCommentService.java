package com.spring.board.service;

import com.spring.board.domain.ArticleComment;
import com.spring.board.dto.ArticleCommentDto;
import com.spring.board.repository.ArticleCommentRepository;
import com.spring.board.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleCommentService {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    @Transactional(readOnly = true)
    public List<ArticleCommentDto> searchArticleComments(Long articleId) {
        return articleCommentRepository.findByArticle_Id(articleId)
                .stream()
                .map(ArticleCommentDto::from)
                .collect(Collectors.toList());
    }

    public void saveArticleComment(ArticleCommentDto dto) {
//        try {
//            articleCommentRepository.save(dto.toEntity(articleRepository.getReferenceById(dto.articleId())))
//        } catch (EntityNotFoundException e) {
//            throw new EntityNotFoundException("댓글 저장 실패 댓글의 게시글을 찾을 수 없습니다 -dto {}: " + dto);
//        }
    }

    public void updateArticleComment(ArticleCommentDto dto) {
        try {
            ArticleComment articleComment = articleCommentRepository.getReferenceById(dto.id());
            if (dto.content() != null) {
                articleComment.updateArticleContent(dto.content());
            }
        }catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("댓글 수정 실패 댓글을 찾을 수 없습니다 -dto {}: " + dto);
        }
    }

    public void deleteArticleComment(Long articleCommentId) {
        articleCommentRepository.deleteById(articleCommentId);
    }
}
