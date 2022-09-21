package io.github.choijy.searchservice.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.choijy.searchservice.domain.dto.Post;
import io.github.choijy.searchservice.domain.request.Sort;
import io.github.choijy.searchservice.domain.response.BlogPostResponse;
import io.github.choijy.searchservice.service.SearchService;
import lombok.RequiredArgsConstructor;

/**
 * Description : 블로그 검색 Controller class.
 *
 * Created by jychoi on 2022/09/21.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/search")
public class SearchController {

	private final SearchService searchService;

	/**
	 * 블로그 검색.
	 * @param keyword 검색 키워드
	 * @param page 페이지
	 * @param pageSize 페이지 사이즈
	 * @param sort 정렬 방식 (LATEST | ACCURACY)
	 * @return BlogPostResponse
	 */
	@GetMapping
	public ResponseEntity<BlogPostResponse> getBlogPosts(@RequestParam String keyword,
		@RequestParam int page,
		@RequestParam(name = "page-size") int pageSize,
		@RequestParam Sort sort) {
		Post searchResult = searchService.getPost(keyword, sort, PageRequest.of(page, pageSize));
		return ResponseEntity.ok(new BlogPostResponse(searchResult));
	}
}
