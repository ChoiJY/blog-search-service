package io.github.choijy.searchservice.domain.response;

import java.util.Collections;
import java.util.List;

import org.springframework.util.ObjectUtils;

import io.github.choijy.searchservice.domain.dto.Post;
import lombok.Getter;

/**
 * Description : 블로그 검색 결과 response class.
 *
 * Created by jychoi on 2022/09/21.
 */
@Getter
public class BlogPostResponse extends Response {

	private final List<Post.Document> responses;

	public BlogPostResponse(Post post) {
		super();
		this.responses = ObjectUtils.isEmpty(post.getDocuments()) ? Collections.emptyList() : post.getDocuments();
	}
}
