package io.github.choijy.statisticsservice.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import io.github.choijy.statisticsservice.domain.SearchHistory;

/**
 * Description : Statistics Service test class.
 *
 * Created by jychoi on 2022/09/19.
 */
@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {

	@InjectMocks
	private StatisticsService statisticsService;

	@Mock
	private RedisTemplate<String, String> redisTemplate;

	@Mock
	private ZSetOperations<String, String> zSetOperations;

	private final String rankingKey = "search-rank";

	@DisplayName("인기 검색어 top 10 조회")
	@Nested
	class GetBlogSearchTop10Test {

		@DisplayName("성공 - 정상 조회")
		@Test
		void getSearchHistoryTop10() {
			when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
			when(zSetOperations.reverseRangeWithScores(rankingKey, 0, 10))
				.thenReturn(Set.of(new DefaultTypedTuple<>("카카오", 30.0),
					new DefaultTypedTuple<>("네이버", 23.0)));

			List<SearchHistory> actual = statisticsService.getSearchHistoryTop10();
			List<SearchHistory> expected = Arrays.asList(new SearchHistory("카카오", 30), new SearchHistory("네이버", 23));

			verify(redisTemplate, times(1)).opsForZSet();
			verify(zSetOperations, times(1)).reverseRangeWithScores(rankingKey, 0, 10);
			assertThat(actual.get(0).getKeyword()).isEqualTo(expected.get(0).getKeyword());
			assertThat(actual.get(0).getCount()).isEqualTo(expected.get(0).getCount());
			assertThat(actual.get(1).getKeyword()).isEqualTo(expected.get(1).getKeyword());
			assertThat(actual.get(1).getCount()).isEqualTo(expected.get(1).getCount());
		}

		@DisplayName("성공 - 데이터가 없는 경우 빈 Set 반환")
		@Test
		void getSearchHistoryTop10NoData() {
			when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
			when(zSetOperations.reverseRangeWithScores(rankingKey, 0, 10)).thenReturn(Collections.emptySet());

			List<SearchHistory> actual = statisticsService.getSearchHistoryTop10();
			List<SearchHistory> expected = Collections.emptyList();

			verify(redisTemplate, times(1)).opsForZSet();
			verify(zSetOperations, times(1)).reverseRangeWithScores(rankingKey, 0, 10);
			assertThat(actual).isEqualTo(expected);
		}

		@DisplayName("실패 - redis exception")
		@Test
		void getSearchHistoryTop10Failed() {
			when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
			when(zSetOperations.reverseRangeWithScores(rankingKey, 0, 10)).thenThrow(new NullPointerException());

			assertThrows(NullPointerException.class, () -> statisticsService.getSearchHistoryTop10());

			verify(redisTemplate, times(1)).opsForZSet();
			verify(zSetOperations, times(1)).reverseRangeWithScores(rankingKey, 0, 10);
		}
	}

	@DisplayName("검색 키워드 기록 count 업데이트")
	@Nested
	class IncreaseSearchHistoryCountTest {

		private final String searchKeyword = "검색어";

		@DisplayName("성공 - 정상 업데이트")
		@Test
		void increaseSearchHistoryCount() {
			when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
			when(zSetOperations.incrementScore(rankingKey, searchKeyword, 1)).thenReturn(1.0);

			statisticsService.increaseSearchHistoryCount(searchKeyword);

			verify(redisTemplate, times(1)).opsForZSet();
			verify(zSetOperations, times(1)).incrementScore(rankingKey, searchKeyword, 1);
		}

		@DisplayName("실패 - redis exception")
		@Test
		void increaseSearchHistoryCountFailed() {
			when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
			when(zSetOperations.incrementScore(rankingKey, searchKeyword, 1)).thenThrow(new NullPointerException());

			assertThrows(NullPointerException.class, () -> statisticsService.increaseSearchHistoryCount(searchKeyword));

			verify(redisTemplate, times(1)).opsForZSet();
			verify(zSetOperations, times(1)).incrementScore(rankingKey, searchKeyword, 1);
		}
	}
}