package io.github.choijy.searchservice.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import io.github.choijy.searchservice.configuration.AuthProperties;
import io.github.choijy.searchservice.domain.dto.Post;
import io.github.choijy.searchservice.exception.KakaoApiException;

/**
 * Description : Kakao 검색 API test class.
 *
 * Created by jychoi on 2022/09/21.
 */
@RestClientTest(value = {KakaoService.class, AuthProperties.class})
@AutoConfigureWebClient(registerRestTemplate = true)
class KakaoServiceTest {

	@Autowired
	private KakaoService kakaoService;

	@Autowired
	private MockRestServiceServer mockRestServiceServer;

	@Autowired
	private AuthProperties authProperties;

	private final String url = "https://dapi.kakao.com//v2/search/web";

	@BeforeEach
	void setUp() {
		kakaoService = new KakaoService(new RestTemplate(), authProperties);
	}

	@DisplayName("카카오 API 조회 - RECENCY")
	@Test
	void getKakaoPostTypeRecency() {
		mockRestServiceServer.expect(requestTo(url + "?query=카카오&size=10&page=1&sort=recency"))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess());

		ResponseEntity<Post> result = kakaoService.getPosts("카카오", "recency", PageRequest.of(1, 10));

		assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(Objects.requireNonNull(result.getBody()).getMeta().getIsEnd()).isFalse();
	}

	@DisplayName("카카오 API 조회 - ACCURACY")
	@Test
	void getKakaoPostTypeAccuracy() {
		mockRestServiceServer.expect(requestTo(url + "?query=카카오&size=50&page=50"))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess());

		ResponseEntity<Post> result = kakaoService.getPosts("카카오", "accuracy", PageRequest.of(50, 50));

		assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(Objects.requireNonNull(result.getBody()).getMeta().getIsEnd()).isTrue();
	}

	@DisplayName("카카오 API 조회 실패 - 필수 파라미터 누락")
	@Test
	void getKakaoPostFailed() {
		mockRestServiceServer.expect(requestTo(url + "?display=10&start=1"))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withBadRequest());

		assertThrows(KakaoApiException.class, () -> kakaoService.getPosts(null, "accuracy", PageRequest.of(1, 10)));
	}
}