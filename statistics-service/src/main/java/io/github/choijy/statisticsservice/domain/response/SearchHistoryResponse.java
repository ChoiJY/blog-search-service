package io.github.choijy.statisticsservice.domain.response;

import java.util.List;

import io.github.choijy.statisticsservice.domain.SearchHistory;
import lombok.Getter;

/**
 * Description : 인기 검색어 Response class.
 * <p>
 * Created by jychoi on 2022/09/17.
 */
@Getter
public class SearchHistoryResponse extends Response {

    private final List<SearchHistory> responses;

    public SearchHistoryResponse(List<SearchHistory> searchHistories) {
        super();
        this.responses = searchHistories;
    }
}
