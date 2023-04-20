package com.skillup.infrastructure.redis;

import com.alibaba.fastjson2.JSON;
import com.skillup.domain.promotionCache.PromotionCacheDomain;
import com.skillup.domain.promotionCache.PromotionCacheRepo;
import com.skillup.domain.stock.StockDomain;
import com.skillup.domain.stock.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class RedisRepo implements StockRepository, PromotionCacheRepo {

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

    @Override
    public boolean lockAvailableStock(StockDomain stockDomain) {
        return false;
    }

    @Override
    public boolean revertAvailableStock(StockDomain stockDomain) {
        return false;
    }

    public Long getPromotionAvailableStock(String promotionId) {
        // 解包
        // create stock key
        String key = StockDomain.createStockKey(promotionId);
        return JSON.parseObject(get(key), Long.class);
    }

    @Override
    public void setPromotionAvailableStock(String promotionId, Long availableStock) {
        // create stock key
        String key = StockDomain.createStockKey(promotionId);
        set(key, availableStock);
    }

    @Override
    public PromotionCacheDomain getPromotionById(String id) {
        return JSON.parseObject(get(id), PromotionCacheDomain.class);
    }

    @Override
    public void setPromotion(PromotionCacheDomain cacheDomain) {
        set(cacheDomain.getPromotionId(), cacheDomain);
    }
}