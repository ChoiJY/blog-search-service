package io.github.choijy.searchservice.exception;

/**
 * Description : Naver API excpetion class.
 *
 * Created by jychoi on 2022/09/21.
 */
public class NaverApiException extends RuntimeException {

	public NaverApiException(Exception e) {
		super(e);
	}
}
