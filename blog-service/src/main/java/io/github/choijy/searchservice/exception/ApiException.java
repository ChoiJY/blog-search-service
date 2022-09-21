package io.github.choijy.searchservice.exception;

/**
 * Description : API exception class.
 *
 * Created by jychoi on 2022/09/21.
 */
public class ApiException extends RuntimeException {

	public ApiException(String message) {
		super(message);
	}
}
