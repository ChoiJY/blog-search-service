package io.github.choijy.searchservice.domain.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Description : 검색 요청 enum class.
 *
 * Created by jychoi on 2022/09/21.
 */
@RequiredArgsConstructor
@Getter
public enum Sort {

	LATEST("date", "recency"),
	ACCURACY("sim", "accuracy");

	private final String naverVariable;
	private final String kakaoVariable;
}
