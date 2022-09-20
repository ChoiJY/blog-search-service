package io.github.choijy.statisticsservice.service;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Objects;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import io.github.choijy.statisticsservice.domain.SearchHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Description : 인기 검색어 Service class.
 *
 * Created by jychoi on 2022/09/17.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class StatisticsService {

	private static final String RANKING_KEY = "search-rank";
	private final RedisTemplate<String, String> redisTemplate;

	/**
	 * 인기 검색어 top 10 조회.
	 * @return SearchHistory list
	 */
	public List<SearchHistory> getSearchHistoryTop10() {
		ZSetOperations<String, String> stringZSetOperations = redisTemplate.opsForZSet();
		return Objects.requireNonNull(stringZSetOperations.reverseRangeWithScores(RANKING_KEY, 0, 10))
			.stream()
			.map(each -> new SearchHistory(each.getValue(), Objects.requireNonNull(each.getScore()).longValue()))
			.collect(toList());
	}

	/**
	 * 검색 키워드 기록 count 업데이트.
	 * @param searchKeyword 검섹 키워드
	 */
	public void increaseSearchHistoryCount(String searchKeyword) {
		ZSetOperations<String, String> stringZSetOperations = redisTemplate.opsForZSet();
		Double result = stringZSetOperations.incrementScore(RANKING_KEY, searchKeyword, 1);
		log.info("조회수 count 증가, keyword = {}, result = {}", searchKeyword, result);
	}
}
