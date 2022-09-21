package io.github.choijy.searchservice.domain.response;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * Description : 공통 Response abstract class.
 *
 * Created by jychoi on 2022/09/17.
 */
@Getter
public abstract class Response {

	private final int code;

	public Response(int code) {
		this.code = code;
	}

	public Response() {
		this.code = HttpStatus.OK.value();
	}
}
