package com.skillup.infrastructure.redis;

import com.alibaba.fastjson2.JSON;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

// 加了@SpringBootTest，所有spring的环境就都有了
@SpringBootTest
public class RedisRepoTest {
    @Autowired
    RedisRepo redisRep;

    public static String KEY = "name";
    public static String VALUE = "skillup";

    // @Test执行完后才来到@AfterEach
    @AfterEach
    public void cleanDataCreate() {
        // clean data
        redisRep.set(KEY, "Clean it!");
        System.out.println("--- Clean Data ---");
    }

    @Test
    void setAndGetThenDeleteValueTest() {
        redisRep.set(KEY, VALUE);
        assertEquals(VALUE, JSON.parseObject(redisRep.get(KEY), String.class));
    }

}
