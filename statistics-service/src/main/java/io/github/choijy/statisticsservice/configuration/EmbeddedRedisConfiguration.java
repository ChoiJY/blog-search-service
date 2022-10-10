package io.github.choijy.statisticsservice.configuration;

import static java.nio.file.attribute.PosixFilePermission.GROUP_EXECUTE;
import static java.nio.file.attribute.PosixFilePermission.GROUP_READ;
import static java.nio.file.attribute.PosixFilePermission.OWNER_EXECUTE;
import static java.nio.file.attribute.PosixFilePermission.OWNER_READ;
import static java.nio.file.attribute.PosixFilePermission.OWNER_WRITE;

import io.github.choijy.statisticsservice.exception.EmbeddedRedisServerException;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import redis.embedded.RedisServer;

/**
 * Description : Embedded Redis Configuration class.
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
    public void afterPropertiesSet() throws EmbeddedRedisServerException {
        int redisPort = redisProperties.getPort();

        if (isArmMac()) {
            redisServer = new RedisServer(Objects.requireNonNull(getRedisFileForArcMac()),
                    redisPort);
        } else {
            redisServer = new RedisServer(redisPort);
        }

        redisServer.start();
    }

    private boolean isArmMac() {
        return Objects.equals(System.getProperty("os.arch"), "aarch64") &&
                Objects.equals(System.getProperty("os.name"), "Mac OS X");
    }

    private File getRedisFileForArcMac() throws EmbeddedRedisServerException {
        try (InputStream inputStream = new ClassPathResource(
                "/binary/redis-server-m1-arm64").getInputStream()) {
            File file = File.createTempFile("redis-server-m1-arm64", null);
            FileUtils.copyInputStreamToFile(inputStream, file);
            Files.setPosixFilePermissions(file.toPath(),
                    EnumSet.of(OWNER_READ, OWNER_WRITE, OWNER_EXECUTE, GROUP_READ, GROUP_EXECUTE));
            return file;
        } catch (Exception e) {
            throw new EmbeddedRedisServerException();
        }
    }
}
