package io.github.choijy.statisticsservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.choijy.statisticsservice.domain.response.SearchHistoryResponse;
import io.github.choijy.statisticsservice.service.StatisticsService;
import lombok.RequiredArgsConstructor;

/**
 * Description : 인기 검색어 관련 controller class.
 * <p>
 * Created by jychoi on 2022/09/17.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    /**
     * 인기 검색어 Top 10 조회.
     *
     * @return SearchHistoryResponse
     */
    @GetMapping("/blog-search/top10")
    public ResponseEntity<SearchHistoryResponse> getBlogSearchTop10() {
        return ResponseEntity.ok()
                .body(new SearchHistoryResponse(statisticsService.getSearchHistoryTop10()));
    }

    /**
     * 검색 키워드 기록 count 업데이트.
     *
     * @param searchKeyword 검색 키워드
     * @return Void
     */
    @PutMapping("/blog-search/{search-keyword}")
    public ResponseEntity<Void> updateSearchHistory(
            @PathVariable(name = "search-keyword") String searchKeyword) {
        statisticsService.increaseSearchHistoryCount(searchKeyword);
        return ResponseEntity.ok().build();
    }
}
