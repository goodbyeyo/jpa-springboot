package com.spring.board.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@DisplayName("비지니스 로직 - 페이지네이션 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = PaginationService.class)  // 테스트 경량화
class PaginationServiceTest {

    private final PaginationService sut;

    public PaginationServiceTest(@Autowired PaginationService paginationService) {
        this.sut = paginationService;
    }

    @DisplayName("현재 페이지 번호와 총 페이지 수를 주면, 페이징 바 리스트를 만들어준다")
    @MethodSource
    @ParameterizedTest(name ="[{index}] {0}, {1} => {2}")
    void givenCurrentPageNumberAndTotalPages_whenCalculating_thenReturnsPaginationBarNumber(
            int currentPageNumber, int totalPages, List<Integer> expected) {
        // given

        // when
        List<Integer> actual = sut.getPaginationBarNumbers(currentPageNumber, totalPages);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> givenCurrentPageNumberAndTotalPages_whenCalculating_thenReturnsPaginationBarNumber() {
        return Stream.of(
                Arguments.of(0, 13, List.of(0, 1, 2, 3, 4)),
                Arguments.of(1, 13, List.of(0, 1, 2, 3, 4)),
                Arguments.of(2, 13, List.of(0, 1, 2, 3, 4)),
                Arguments.of(3, 13, List.of(1, 2, 3, 4, 5)),
                Arguments.of(4, 13, List.of(2, 3, 4, 5, 6)),
                Arguments.of(5, 13, List.of(3, 4, 5, 6, 7)),
                Arguments.of(6, 13, List.of(4, 5, 6, 7, 8)),
                Arguments.of(10, 13, List.of(8, 9, 10, 11, 12)),
                Arguments.of(11, 13, List.of(9, 10, 11, 12)),
                Arguments.of(12, 13, List.of(10, 11, 12))
        );
    }

    @DisplayName("현재 설정되어 있는 페이지네이션 바의 길이를 알려준다")
    @Test
    void givenNothing_whenCalling_thenReturnsCurrentBarLength() {
        // given

        // when
        int actual = sut.currentBarLength();

        // then
        assertThat(actual).isEqualTo(5);
    }
}