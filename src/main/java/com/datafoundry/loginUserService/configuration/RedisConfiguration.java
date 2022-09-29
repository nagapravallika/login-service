package com.datafoundry.loginUserService.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisConfiguration  {

    @Value(value = "${spring.redis.host}")
    private String redisHostName;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Value(value = "${spring.redis.port}")
    private int redisPortNo;
    
    @Value(value="${spring.redis.database}")
    private int database;

    @Bean
    public JedisConnectionFactory connectionFactory() {
        
    	RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        
        JedisClientConfiguration jedisClientConfig = JedisClientConfiguration.builder().usePooling().and().useSsl().build();
        
        
        redisConfig.setDatabase(database);
        redisConfig.setHostName(redisHostName);
        redisConfig.setPassword(redisPassword);
        redisConfig.setPort(redisPortNo);
        
        JedisConnectionFactory connFactory= new JedisConnectionFactory(redisConfig,jedisClientConfig);
        
        return connFactory;
    }
    
    @Bean
    RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
 
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        
        redisTemplate.setConnectionFactory(connectionFactory);
        
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        
        return redisTemplate;
 
    }


}