package io.github.choijy.searchservice.controller;

import static java.nio.charset.StandardCharsets.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import io.github.choijy.searchservice.domain.dto.Post;
import io.github.choijy.searchservice.domain.request.Sort;
import io.github.choijy.searchservice.service.SearchService;

/**
 * Description : 블로그 검색 Controller test class.
 *
 * Created by jychoi on 2022/09/21.
 */
@WebMvcTest(SearchController.class)
class SearchControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	SearchService searchService;

	@DisplayName("블로그 검색")
	@Test
	void getBlogPosts() throws Exception {
		String searchKeyword = "test";
		int page = 1;
		int pageSize = 10;
		Sort sort = Sort.ACCURACY;
		when(searchService.getPost(searchKeyword, sort, PageRequest.of(page, pageSize)))
			.thenReturn(new Post(List.of(new Post.Document("test1", "12345", "www.kakao.com")),
				new Post.Meta(false, 100L, 1000L),
				10000L, 1L, 10L));

		MvcResult mvcResult = mockMvc.perform(get("/v1/search")
				.param("keyword", searchKeyword)
				.param("page", String.valueOf(page))
				.param("page-size", String.valueOf(pageSize))
				.param("sort", sort.name()))
			.andExpect(status().isOk())
			.andReturn();

		assertThat(mvcResult.getResponse().getContentAsString(UTF_8))
			.isEqualTo(
				"{\"code\":200,\"responses\":[{\"title\":\"test1\",\"content\":\"12345\",\"url\":\"www.kakao.com\"}]}");
	}

	@DisplayName("블로그 검색 실패 - 필수 파라미터 누락")
	@Test
	void getBlogPostsFailed() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/v1/search")
				.param("keyword", "test")
				.param("page-size", "10")
				.param("sort", "ACCURACY"))
			.andExpect(status().isBadRequest())
			.andReturn();

		assertThat(mvcResult.getResponse().getContentAsString(UTF_8))
			.contains(
				"{\"code\":400,\"message\":\"Required request parameter 'page' for method parameter type int is not present\"}");
	}
}