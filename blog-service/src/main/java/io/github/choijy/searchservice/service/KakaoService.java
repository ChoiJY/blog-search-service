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
import io.github.choijy.searchservice.exception.KakaoApiException;
import lombok.RequiredArgsConstructor;

/**
 * Description : Kakao 블로그 검색 Service class.
 *
 * Created by jychoi on 2022/09/21.
 */
@RequiredArgsConstructor
@Service
public class KakaoService implements BlogService {

	private final RestTemplate restTemplate;
	private final AuthProperties authProperties;

	@Override
	public ResponseEntity<Post> getPosts(String keyword, String sortType, PageRequest pageRequest) {
		URI uri = UriComponentsBuilder
			.fromUriString("https://dapi.kakao.com")
			.path("/v2/search/web")
			.queryParam("query", keyword)
			.queryParam("size", pageRequest.getPageSize())
			.queryParam("page", pageRequest.getPageNumber())
			.queryParam("sort", sortType)
			.encode()
			.build()
			.toUri();

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, authProperties.getKakaoKey());
		HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(headers);

		try {
			return restTemplate.exchange(uri, HttpMethod.GET, httpEntity, Post.class);
		} catch (Exception e) {
			throw new KakaoApiException(e);
		}
	}
}
