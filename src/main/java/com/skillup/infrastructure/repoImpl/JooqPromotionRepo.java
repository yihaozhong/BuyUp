package com.skillup.infrastructure.repoImpl;

import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionRepository;
import com.skillup.domain.promotion.stockStrategy.StockOperation;
import com.skillup.infrastructure.jooq.tables.Promotion;
import com.skillup.infrastructure.jooq.tables.records.PromotionRecord;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository(value = "optimistic")
@Slf4j
public class JooqPromotionRepo implements PromotionRepository, StockOperation {

    @Autowired
    DSLContext dslContext;

    public static final Promotion P_T = new Promotion();
    @Override
    public void createPromotion(PromotionDomain domain){
        dslContext.executeInsert(toRecord(domain));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PromotionDomain getPromotionById(String id) {
        return dslContext.selectFrom(P_T).where(P_T.PROMOTION_ID.eq(id)).fetchOptional(this:: toDomain).orElse(null);
    }

    @Override
    public List<PromotionDomain> getPromotionByStatus(Integer status) {
        return dslContext.selectFrom(P_T).where(P_T.STATUS.eq(status)).fetch(this:: toDomain);
    }

//    @Override
//    public boolean lockStock(String id){
//        // database level: lockStock = get value, set value - 1
//        return false;
//    }

    @Override
    public void updatePromotion(PromotionDomain promotionDomain) {
        dslContext.executeUpdate(toRecord(promotionDomain));
    }


    private PromotionRecord toRecord(PromotionDomain domain){
        PromotionRecord promotionRecord = new PromotionRecord();
        promotionRecord.setPromotionId(domain.getPromotionId());
        promotionRecord.setPromotionName(domain.getPromotionName());
        promotionRecord.setCommodityId(domain.getCommodityId());
        promotionRecord.setOriginalPrice(domain.getOriginalPrice());
        promotionRecord.setPromotionPrice(domain.getPromotionalPrice());
        promotionRecord.setStartTime(domain.getStartTime());
        promotionRecord.setEndTime(domain.getEndTime());
        promotionRecord.setStatus(domain.getStatus());
        promotionRecord.setTotalStock(domain.getTotalStock());
        promotionRecord.setAvailableStock(domain.getAvailableStock());
        promotionRecord.setLockStock(domain.getLockStock());
        promotionRecord.setImageUrl(domain.getImageUrl());
        return promotionRecord;
    }

    private PromotionDomain toDomain(PromotionRecord record) {
        return PromotionDomain.builder()
                .promotionId(record.getPromotionId())
                .promotionName(record.getPromotionName())
                .commodityId(record.getCommodityId())
                .originalPrice(record.getOriginalPrice())
                .promotionalPrice(record.getPromotionPrice())
                .status(record.getStatus())
                .startTime(record.getStartTime())
                .endTime(record.getEndTime())
                .totalStock(record.getTotalStock())
                .availableStock(record.getAvailableStock())
                .lockStock(record.getLockStock())
                .imageUrl(record.getImageUrl())
                .build();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean lockStock(String id) {

        // update goods
        // set stock = stock - 1 where id = 1 and availbale stock > 0;

        log.info("----- Optimistic Strategy Lock Stock-----");
        int isLocked = dslContext.update(P_T)
                .set(P_T.AVAILABLE_STOCK, P_T.AVAILABLE_STOCK.subtract(1))
                .set(P_T.LOCK_STOCK, P_T.LOCK_STOCK.add(1))
                .where(P_T.PROMOTION_ID.eq(id).and(P_T.AVAILABLE_STOCK.greaterThan(0L)))
                .execute();
        return isLocked == 1;

    }

    @Override
    public boolean revertStock(String id) {
        // update goods
        // set stock = stock + 1 where id = promotion_id and lockstock > 0;

        log.info("----- Optimistic Strategy Revert-----");
        int isReversed = dslContext.update(P_T)
                .set(P_T.AVAILABLE_STOCK, P_T.AVAILABLE_STOCK.add(1))
                .set(P_T.LOCK_STOCK, P_T.LOCK_STOCK.subtract(1))
                .where(P_T.PROMOTION_ID.eq(id).and(P_T.LOCK_STOCK.greaterThan(0L)))
                .execute();
        return isReversed == 1;
    }

    @Override
    public boolean deductStock(String id) {
        // update goods
        // set stock = stock - 1 where id = 1 and lock stock > 0;

        log.info("----- Optimistic Strategy Deduct Stock-----");
        int isDeducted = dslContext.update(P_T)
                .set(P_T.LOCK_STOCK, P_T.LOCK_STOCK.subtract(1))
                .where(P_T.PROMOTION_ID.eq(id).and(P_T.AVAILABLE_STOCK.greaterThan(0L)))
                .execute();
        return isDeducted == 1;
    }
}
