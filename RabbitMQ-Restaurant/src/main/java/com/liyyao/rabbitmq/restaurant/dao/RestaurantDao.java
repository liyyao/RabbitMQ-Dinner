package com.liyyao.rabbitmq.restaurant.dao;

import com.liyyao.rabbitmq.restaurant.entity.po.Restaurant;
import org.apache.ibatis.annotations.Select;

/**
 * 餐厅Dao
 */
public interface RestaurantDao {

    @Select("SELECT id, name, address, status, settlement_id, date " +
            "FROM restaurant " +
            "WHERE id=#{id}")
    Restaurant selectRestaurant(String id);
}
