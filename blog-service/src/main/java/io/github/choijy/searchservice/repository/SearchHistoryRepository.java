package io.github.choijy.searchservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.choijy.searchservice.domain.entity.SearchHistoryEntity;

/**
 * Description : 검색 기록 Repository class.
 *
 * Created by jychoi on 2022/09/21.
 */
public interface SearchHistoryRepository extends JpaRepository<SearchHistoryEntity, Long> {
}
