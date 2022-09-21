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
import io.github.choijy.searchservice.exception.NaverApiException;

/**
 * Description : Naver 검색 API test class.
 *
 * Created by jychoi on 2022/09/19.
 */
@RestClientTest(value = {NaverService.class, AuthProperties.class})
@AutoConfigureWebClient(registerRestTemplate = true)
class NaverServiceTest {

	@Autowired
	private NaverService naverService;

	@Autowired
	private MockRestServiceServer mockRestServiceServer;

	@Autowired
	private AuthProperties authProperties;

	private final String url = "https://openapi.naver.com/v1/search/blog.json";

	@BeforeEach
	void setUp() {
		this.naverService = new NaverService(new RestTemplate(), authProperties);
	}

	@DisplayName("네이버 API 조회 - SIM")
	@Test
	void getNaverPostTypeSim() {
		mockRestServiceServer.expect(requestTo(url + "?query=test&display=10&start=1"))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess());

		ResponseEntity<Post> result = naverService.getPosts("test", "sim", PageRequest.of(1, 10));

		assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(Objects.requireNonNull(result.getBody()).getTotalElements()).isNotEqualTo(0);
		assertThat(result.getBody().getStart()).isEqualTo(1);
		assertThat(result.getBody().getDisplay()).isEqualTo(10);
	}

	@DisplayName("네이버 API 조회 - DATE")
	@Test
	void getNaverPostTypeDate() {
		mockRestServiceServer.expect(requestTo(url + "?query=test&display=100&start=10"))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withSuccess());

		ResponseEntity<Post> result = naverService.getPosts("test", "date", PageRequest.of(10, 100));

		assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(Objects.requireNonNull(result.getBody()).getTotalElements()).isNotEqualTo(0);
		assertThat(result.getBody().getStart()).isEqualTo(10);
		assertThat(result.getBody().getDisplay()).isEqualTo(100);
	}

	@DisplayName("네이버 검색 실패 - 필수 파라미터 누락")
	@Test
	void getNaverPostFailed() {
		mockRestServiceServer.expect(requestTo(url + "?display=10&start=1"))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withBadRequest());

		assertThrows(NaverApiException.class, () -> naverService.getPosts(null, "sim", PageRequest.of(1, 10)));
	}
}