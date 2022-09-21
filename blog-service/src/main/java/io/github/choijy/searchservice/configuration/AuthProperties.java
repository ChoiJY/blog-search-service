package io.github.choijy.searchservice.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * Description : API 연동 관련 properties class.
 *
 * Created by jychoi on 2022/09/21.
 */
@Getter
@Setter
@ConfigurationProperties("app.auth")
@Configuration
public class AuthProperties {

	private Kakao kakao;
	private Naver naver;

	public String getKakaoKey() {
		return this.kakao.key;
	}

	public String getNaverClientId() {
		return this.naver.clientId;
	}

	public String getNaverClientKey() {
		return this.naver.clientKey;
	}

	@Getter
	@Setter
	public static final class Kakao {
		private String key;
	}

	@Getter
	@Setter
	public static final class Naver {
		private String clientId;
		private String clientKey;
	}
}
