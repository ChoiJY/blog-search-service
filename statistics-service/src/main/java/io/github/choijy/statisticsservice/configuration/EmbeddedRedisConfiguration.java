package io.github.choijy.statisticsservice.configuration;

import java.util.Optional;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.embedded.RedisServer;

/**
 * Description : Embedded Redis Configuration class.
 *
 * Created by jychoi on 2022/09/18.
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class EmbeddedRedisConfiguration implements InitializingBean, DisposableBean {

	private final RedisProperties redisProperties;
	private RedisServer redisServer;

	/**
	 * Embedded Redis 종료.
	 */
	@Override
	public void destroy() {
		Optional.ofNullable(redisServer).ifPresent(RedisServer::stop);
	}

	/**
	 * Embedded Redis 시작.
	 */
	@Override
	public void afterPropertiesSet() {
		redisServer = new RedisServer(redisProperties.getPort());
		redisServer.start();
	}
}
