package io.github.choijy.searchservice.domain.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Description : 블로그 검색 결과.
 *
 * Created by jychoi on 2022/09/21.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Post {

	@JsonAlias(value = {"items", "documents"})
	private List<Document> documents;

	/* kakao field */
	private Meta meta;
	/* naver field */
	private Long total;
	/* naver field */
	private Long start;
	/* naver field */
	private Long display;

	public long getTotalElements() {
		return (this.meta == null || this.meta.totalCount == null) ? this.total : this.meta.totalCount;
	}

	public long getPageSize() {
		return (this.meta == null || this.meta.pageableCount == null) ? this.display : this.meta.pageableCount;
	}

	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	public static class Meta {

		@JsonProperty(value = "is_end")
		private Boolean isEnd;
		@JsonProperty(value = "total_count")
		private Long totalCount;
		@JsonProperty(value = "pageable_count")
		private Long pageableCount;
	}

	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	public static class Document {

		@JsonAlias(value = "title")
		private String title;
		@JsonAlias(value = "description")
		private String content;
		@JsonAlias(value = "link")
		private String url;
	}
}
