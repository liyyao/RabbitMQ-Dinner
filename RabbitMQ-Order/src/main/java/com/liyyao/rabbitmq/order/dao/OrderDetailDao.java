package com.liyyao.rabbitmq.order.dao;

import com.liyyao.rabbitmq.order.entity.po.OrderDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface OrderDetailDao {

    @Insert("INSERT INTO order_detail (id, status, address, account_id, product_id, deliveryman_id, settlement_id, reward_id, price, date) " +
            "VALUES (#{id}, #{status}, #{address}, #{accountId}, #{productId}, #{deliverymanId}, #{settlementId}, #{rewardId}, #{price}, #{date})")
    void save(OrderDetail orderDetail);

    @Update("UPDATE order_detail SET status=#{status}, address=#{address}, account_id=#{accountId}, product_id=#{productId}, deliveryman_id=#{deliverymanId}," +
            "settlement_id=#{settlementId}, reward_id=#{rewardId}, price=#{price}, date=#{date} " +
            "WHERE id=#{id}")
    void update(OrderDetail orderDetail);

    @Select("SELECT id, status, address, account_id as AccountId, product_id as productId, deliveryman_id as deliverymanId, settlement_id as settlementId, reward_id as rewardId, price, date " +
            "FROM order_detail " +
            "WHERE id=#{id}")
    OrderDetail selectOrder(String id);
}
