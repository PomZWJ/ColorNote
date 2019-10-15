package com.bluefatty.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis常用的操作
 * @author PomZWJ
 * @github https://github.com/PomZWJ
 * @date 2019-10-15
 */
@Component
public class RedisUtils {
    @Autowired
    private RedisTemplate<Object,Object>redisTemplate;

    public void cacheString(String key, String value, long expire, TimeUnit timeUnit){
        this.redisTemplate.opsForValue().set(key,value,expire,timeUnit);
    }
    public String getCacheString(String key){
        Object value = this.redisTemplate.opsForValue().get(key);
        return StringUtils.getValue(value);
    }
}
