package com.liyyao.rabbitmq.reward.dao;

import com.liyyao.rabbitmq.reward.entity.po.Reward;
import org.apache.ibatis.annotations.Insert;

/**
 * 奖励服务Dao
 */
public interface RewardDao {

    @Insert("INSERT INTO reward (id, order_id, amount, status, date) " +
            "VALUES (#{id}, #{orderId}, #{amount}, #{status}, #{date})")
    void insert(Reward reward);

}
