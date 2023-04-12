/*
 * This file is generated by jOOQ.
 */
package com.skillup.infrastructure.jooq;


import com.skillup.infrastructure.jooq.tables.Commodity;
import com.skillup.infrastructure.jooq.tables.Order;
import com.skillup.infrastructure.jooq.tables.Promotion;
import com.skillup.infrastructure.jooq.tables.User;
import com.skillup.infrastructure.jooq.tables.records.CommodityRecord;
import com.skillup.infrastructure.jooq.tables.records.OrderRecord;
import com.skillup.infrastructure.jooq.tables.records.PromotionRecord;
import com.skillup.infrastructure.jooq.tables.records.UserRecord;

import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in 
 * skillup.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<CommodityRecord> KEY_COMMODITY_PRIMARY = Internal.createUniqueKey(Commodity.COMMODITY, DSL.name("KEY_commodity_PRIMARY"), new TableField[] { Commodity.COMMODITY.COMMODITY_ID }, true);
    public static final UniqueKey<OrderRecord> KEY_ORDER_PRIMARY = Internal.createUniqueKey(Order.ORDER, DSL.name("KEY_order_PRIMARY"), new TableField[] { Order.ORDER.ORDER_NUMBER }, true);
    public static final UniqueKey<PromotionRecord> KEY_PROMOTION_PRIMARY = Internal.createUniqueKey(Promotion.PROMOTION, DSL.name("KEY_promotion_PRIMARY"), new TableField[] { Promotion.PROMOTION.PROMOTION_ID }, true);
    public static final UniqueKey<UserRecord> KEY_USER_PRIMARY = Internal.createUniqueKey(User.USER, DSL.name("KEY_user_PRIMARY"), new TableField[] { User.USER.USER_ID }, true);
    public static final UniqueKey<UserRecord> KEY_USER_USER_NAME = Internal.createUniqueKey(User.USER, DSL.name("KEY_user_user_name"), new TableField[] { User.USER.USER_NAME }, true);
}
