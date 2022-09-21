package io.github.choijy.searchservice.service;

import java.net.URI;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import io.github.choijy.searchservice.domain.entity.SearchHistoryEntity;
import io.github.choijy.searchservice.exception.StatisticsApiException;
import io.github.choijy.searchservice.repository.SearchHistoryRepository;
import lombok.RequiredArgsConstructor;

/**
 * Description : 인기 검색어 관련 Service class.
 *
 * Created by jychoi on 2022/09/21.
 */
@RequiredArgsConstructor
@Service
public class SearchHistoryService {

	private final SearchHistoryRepository searchHistoryRepository;
	private final RestTemplate restTemplate;

	/**
	 * 검색 키워드 기록 count update API 호출 / local db 내 기록 저장.
	 * @param searchKeyword 검색 키워드
	 */
	public void updateSearchHistory(String searchKeyword) {
		URI uri = UriComponentsBuilder
			.fromUriString("http://localhost:8081")
			.path("/v1/statistics/blog-search/" + searchKeyword)
			.build()
			.encode()
			.toUri();

		try {
			restTemplate.put(uri, null);
			searchHistoryRepository.save(new SearchHistoryEntity(searchKeyword));
		} catch (RestClientException e) {
			throw new StatisticsApiException(e.getMessage());
		}
	}
}
