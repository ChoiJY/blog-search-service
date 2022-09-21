package io.github.choijy.searchservice.service;

import java.net.URI;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import io.github.choijy.searchservice.configuration.AuthProperties;
import io.github.choijy.searchservice.domain.dto.Post;
import io.github.choijy.searchservice.exception.NaverApiException;
import lombok.RequiredArgsConstructor;

/**
 * Description : Naver 블로그 검색 Service class.
 *
 * Created by jychoi on 2022/09/21.
 */
@RequiredArgsConstructor
@Service
public class NaverService implements BlogService {

	private final RestTemplate restTemplate;
	private final AuthProperties authProperties;

	/**
	 * 블로그 API 조회.
	 * @param keyword 검색 키워드
	 * @param sortType 정렬 방식
	 * @param pageRequest Paging
	 * @return Post
	 */
	@Override
	public ResponseEntity<Post> getPosts(String keyword, String sortType, PageRequest pageRequest) {
		URI uri = UriComponentsBuilder
			.fromUriString("https://openapi.naver.com")
			.path("/v1/search/blog.json")
			.queryParam("query", keyword)
			.queryParam("display", pageRequest.getPageSize())
			.queryParam("start", pageRequest.getPageNumber())
			.queryParam("sort", sortType)
			.encode()
			.build()
			.toUri();

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-Naver-Client-Id", authProperties.getNaverClientId());
		headers.add("X-Naver-Client-Secret", authProperties.getNaverClientKey());
		HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(headers);

		try {
			return restTemplate.exchange(uri, HttpMethod.GET, httpEntity, Post.class);
		} catch (Exception e) {
			throw new NaverApiException(e);
		}
	}
}
