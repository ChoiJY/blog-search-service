package io.github.choijy.statisticsservice.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Description : 인기 검색어 domain class.
 * <p>
 * Created by jychoi on 2022/09/17.
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class SearchHistory {

    private final String keyword;
    private final long count;
}
