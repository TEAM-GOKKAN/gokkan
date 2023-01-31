package com.gokkan.gokkan.global.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private int port;

	@Value("${spring.redis.password}")
	private String password;

//	@Bean
//	public LettuceConnectionFactory redisConnectionFactory() {
//		return new LettuceConnectionFactory(host, port);
//	}

	@Bean
	@Primary
	public LettuceConnectionFactory redisConnectionFactory(
		RedisConfiguration defaultRedisConfig) {
		LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
			.build();

		return new LettuceConnectionFactory(defaultRedisConfig, clientConfig);
	}

	@Bean
	public RedisConfiguration defaultRedisConfig() {
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
		config.setHostName(host);
		config.setPort(port);
		config.setPassword(RedisPassword.of(password));
		return config;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		redisTemplate.setConnectionFactory(redisConnectionFactory(defaultRedisConfig()));
		return redisTemplate;
	}

	@Bean
	public StringRedisTemplate stringRedisTemplate() {
		final StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
		stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
		stringRedisTemplate.setValueSerializer(new StringRedisSerializer());
		stringRedisTemplate.setConnectionFactory(redisConnectionFactory(defaultRedisConfig()));
		return stringRedisTemplate;
	}
}
