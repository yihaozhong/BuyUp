package com.skillup.application.promotion;

import com.skillup.apiPresentation.dto.mapper.PromotionMapper;
import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionService;
import com.skillup.domain.stock.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.List;

// 之所以放到application层是因为这里涉及到三个domain
// 该application用来做缓存预热，在主程序执行完就立刻执行
@Service
@Slf4j
public class PromotionPreHeatApplication implements ApplicationRunner {
    @Autowired
    PromotionService promotionService;
    @Autowired
    StockService stockService;
    @Autowired
    PromotionCacheService promotionCacheService;

    // 当项目启动，该方法被执行，
    // 查找并拿到当前所有promotion状态为1的stock service，生成相应的key和value，放到cache上
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("---- Cache preheat Init active promotion stock into Cache ----");
        // 1. 找到所有的active promotions
        List<PromotionDomain> activePromotions = promotionService.getPromotionByStatus(1);
        activePromotions.forEach(promotionDomain -> {
            // 2. Set available stock to cache
            stockService.setAvailableStock(promotionDomain.getPromotionId(), promotionDomain.getAvailableStock());
            // 3. Set promotion cache domain
            promotionCacheService.setPromotion(PromotionMapper.INSTANCE.toCacheDomain(promotionDomain));
        });
        // 4. save List<PromotionDomain> activePromotions to cache
    }

}
