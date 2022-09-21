package io.github.choijy.searchservice.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.github.choijy.searchservice.domain.dto.Post;
import io.github.choijy.searchservice.domain.request.Sort;
import io.github.choijy.searchservice.exception.ApiException;
import io.github.choijy.searchservice.exception.KakaoApiException;
import io.github.choijy.searchservice.exception.NaverApiException;
import io.github.choijy.searchservice.exception.StatisticsApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Description : 블로그 검색 Service class.
 *
 * Created by jychoi on 2022/09/21.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SearchService {

	private final KakaoService kakaoService;
	private final NaverService naverService;
	private final SearchHistoryService searchHistoryService;

	/**
	 * 블로그 검색.
	 * @param keyword 검색 키워드
	 * @param sort 정렬 방식
	 * @param pageRequest Paging
	 * @return Post
	 */
	public Post getPost(String keyword, Sort sort, PageRequest pageRequest) throws ApiException {

		try {
			ResponseEntity<Post> response = kakaoService.getPosts(keyword, sort.getKakaoVariable(), pageRequest);
			searchHistoryService.updateSearchHistory(keyword);
			if (response.getStatusCode().value() == HttpStatus.OK.value()) {
				return response.getBody();
			}
		} catch (KakaoApiException e) {
			ResponseEntity<Post> response = naverService.getPosts(keyword, sort.getNaverVariable(), pageRequest);
			searchHistoryService.updateSearchHistory(keyword);
			if (response.getStatusCode().value() == HttpStatus.OK.value()) {
				return response.getBody();
			}
		} catch (NaverApiException e) {
			throw new ApiException("API 에러 발생");
		} catch (StatisticsApiException e) {
			throw new ApiException("Statistics service 에러 발생");
		}
		throw new ApiException("기타 에러 발생");
	}

}
