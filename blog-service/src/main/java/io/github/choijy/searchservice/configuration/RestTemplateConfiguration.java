package io.github.choijy.searchservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Description : RestTemplate configuration class.
 *
 * Created by jychoi on 2022/09/21.
 */
@Configuration
public class RestTemplateConfiguration {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
