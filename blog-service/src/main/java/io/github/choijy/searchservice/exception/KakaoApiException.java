package io.github.choijy.searchservice.exception;

/**
 * Description : Kakao API exception class.
 *
 * Created by jychoi on 2022/09/21.
 */
public class KakaoApiException extends RuntimeException {

	public KakaoApiException(Exception e) {
		super(e);
	}
}
