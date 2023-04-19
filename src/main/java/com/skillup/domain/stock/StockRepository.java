package com.skillup.domain.stock;

public interface StockRepository {
    boolean lockStock(StockDomain stockDomain);

    boolean revertStock(StockDomain stockDomain);

    Long getPromotionAvailableStock(String promotionId);

    void setPromotionAvailableStock(String promotionId, Long availableStock);

}
