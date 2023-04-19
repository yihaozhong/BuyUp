package com.skillup.infrastructure.redis;

import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class RedisRepo{

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    public void set(String key, Object value) {

        redisTemplate.opsForValue().set(key, JSON.toJSONString(value));
    }

    public String get(String key) {
        if (Objects.isNull(key)) return null;

        // json string parse need define class

        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key){
        if(Objects.isNull(key)) return;
        redisTemplate.opsForValue().getAndDelete(key);
    }
}