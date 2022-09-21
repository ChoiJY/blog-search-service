package io.github.choijy.searchservice.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import io.github.choijy.searchservice.domain.entity.SearchHistoryEntity;

/**
 * Description : Search History repository test class.
 *
 * Created by jychoi on 2022/09/21.
 */
@DataJpaTest
class SearchHistoryRepositoryTest {

	@Autowired
	private SearchHistoryRepository searchHistoryRepository;

	@DisplayName("검색 기록 저장 테스트")
	@Test
	void searchHistorySave() {
		SearchHistoryEntity searchHistory = new SearchHistoryEntity("test");
		searchHistoryRepository.save(searchHistory);

		SearchHistoryEntity searchHistory2 = new SearchHistoryEntity("test2");
		searchHistoryRepository.save(searchHistory2);

		List<SearchHistoryEntity> searchHistoryEntities = searchHistoryRepository.findAll();
		assertThat(searchHistoryEntities.size()).isEqualTo(2);
	}
}