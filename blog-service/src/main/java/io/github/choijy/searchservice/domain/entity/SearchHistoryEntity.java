package io.github.choijy.searchservice.domain.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Description : 검색 기록 entity class.
 *
 * Created by jychoi on 2022/09/21.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "search_history", indexes = {@Index(columnList = "search_keyword")})
@Entity
public class SearchHistoryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "search_keyword")
	private String searchKeyword;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	public SearchHistoryEntity(String searchKeyword) {
		this.searchKeyword = searchKeyword;
		this.createdAt = LocalDateTime.now();
	}
}
