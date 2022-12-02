package com.liyyao.rabbitmq.settlement.dao;

import com.liyyao.rabbitmq.settlement.entity.po.Settlement;
import org.apache.ibatis.annotations.Insert;

/**
 * 商品Dao
 */
public interface SettlementDao {

    @Insert("INSERT INTO settlement (id, order_id, transaction_id, amount, status, date) " +
            "VALUES (#{id}, #{orderId}, #{transactionId}, #{amount}, #{status}, #{date})")
    void insert(Settlement settlement);

}
