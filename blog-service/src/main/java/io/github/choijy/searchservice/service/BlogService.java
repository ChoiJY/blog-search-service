package io.github.choijy.searchservice.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import io.github.choijy.searchservice.domain.dto.Post;

/**
 * Description : Blog 검색 Service class.
 *
 * Created by jychoi on 2022/09/21.
 */
public interface BlogService {

	ResponseEntity<Post> getPosts(String keyword, String sortType, PageRequest pageRequest);
}
