package com.hbr.reggie.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @ClassName RedisConfig
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/19 15:02
 * @Version 1.0
 */
@Configuration
public class RedisConfig extends CachingConfigurerSupport {
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        // 默认的key序列化器为JdkSerializationRedisSerializer，会出现乱码，改为StringRedisSerializer
        redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
}
