package io.github.choijy.searchservice.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import io.github.choijy.searchservice.domain.dto.Post;
import io.github.choijy.searchservice.domain.request.Sort;
import io.github.choijy.searchservice.exception.KakaoApiException;
import io.github.choijy.searchservice.exception.NaverApiException;

/**
 * Description : 블로그 검색 API test class.
 *
 * Created by jychoi on 2022/09/21.
 */
@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

	@InjectMocks
	private SearchService searchService;

	@Mock
	private KakaoService kakaoService;

	@Mock
	private NaverService naverService;

	@Mock
	private SearchHistoryService searchHistoryService;

	@DisplayName("카카오 API 조회 성공")
	@Test
	void getKakaoPost() {
		Post post = new Post(Arrays.asList(new Post.Document("hi", "kakao_document", "http://kakao.com"),
			new Post.Document("hi2", "kakao_document2", "http://kakao.com")), new Post.Meta(false, 1515L, 100L), null,
			null, null);
		ResponseEntity<Post> response = ResponseEntity.ok(post);
		String searchKeyword = "test";

		when(kakaoService.getPosts(searchKeyword, Sort.ACCURACY.getKakaoVariable(), PageRequest.of(1, 10)))
			.thenReturn(response);

		Post result = searchService.getPost(searchKeyword, Sort.ACCURACY, PageRequest.of(1, 10));

		verify(naverService, never()).getPosts(any(), any(), any());
		verify(searchHistoryService, times(1)).updateSearchHistory(searchKeyword);
		assertThat(result.getDocuments().size()).isEqualTo(2);
	}

	@DisplayName("카카오 API 조회 실패시 Naver 조회")
	@Test
	void getNaverPostWhenKakaoFailed() {
		Post post = new Post(Arrays.asList(new Post.Document("hi", "naver_document", "http://naver.com"),
			new Post.Document("hi2", "naver_document2", "http://naver.com")), new Post.Meta(null, null, null), 2525L,
			1L, 10L);
		ResponseEntity<Post> response = ResponseEntity.ok(post);
		String searchKeyword = "test";

		when(kakaoService.getPosts(searchKeyword, Sort.ACCURACY.getKakaoVariable(), PageRequest.of(1, 10)))
			.thenThrow(new KakaoApiException(new Exception()));
		when(naverService.getPosts(searchKeyword, Sort.ACCURACY.getNaverVariable(), PageRequest.of(1, 10)))
			.thenReturn(response);

		Post result = searchService.getPost(searchKeyword, Sort.ACCURACY, PageRequest.of(1, 10));

		verify(kakaoService).getPosts(searchKeyword, Sort.ACCURACY.getKakaoVariable(), PageRequest.of(1, 10));
		verify(naverService).getPosts(searchKeyword, Sort.ACCURACY.getNaverVariable(), PageRequest.of(1, 10));
		verify(searchHistoryService, times(1)).updateSearchHistory(searchKeyword);
		assertThat(result.getDocuments().size()).isEqualTo(2);
	}

	@DisplayName("조회 전체 실패")
	@Test
	void getPostAllFailed() {
		String searchKeyword = "test";
		when(kakaoService.getPosts(searchKeyword, Sort.ACCURACY.getKakaoVariable(), PageRequest.of(1, 10)))
			.thenThrow(new KakaoApiException(new Exception()));
		when(naverService.getPosts(searchKeyword, Sort.ACCURACY.getNaverVariable(), PageRequest.of(1, 10)))
			.thenThrow(new NaverApiException(new Exception()));

		assertThrows(NaverApiException.class,
			() -> searchService.getPost(searchKeyword, Sort.ACCURACY, PageRequest.of(1, 10)));

		verify(kakaoService).getPosts(searchKeyword, Sort.ACCURACY.getKakaoVariable(), PageRequest.of(1, 10));
		verify(naverService).getPosts(searchKeyword, Sort.ACCURACY.getNaverVariable(), PageRequest.of(1, 10));
		verify(searchHistoryService, never()).updateSearchHistory(searchKeyword);
	}
}