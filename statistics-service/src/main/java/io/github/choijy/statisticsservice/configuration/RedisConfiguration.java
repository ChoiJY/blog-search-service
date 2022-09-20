package io.github.choijy.statisticsservice.configuration;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import lombok.RequiredArgsConstructor;

/**
 * Description : Redis Configuration class.
 *
 * Created by jychoi on 2022/09/17.
 */
@RequiredArgsConstructor
@Configuration
@EnableRedisRepositories
public class RedisConfiguration {

	private final RedisProperties redisProperties;

	/**
	 * Redis connection factory bean.
	 * @return LettuceConnectionFactory
	 */
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
	}

	/**
	 * RedisTemplate bean.
	 * @return RedisTemplate
	 */
	@Bean
	public RedisTemplate<?, ?> redisTemplate() {
		RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		return redisTemplate;
	}
}
