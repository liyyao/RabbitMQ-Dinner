package com.liyyao.rabbitmq.restaurant.dao;

import com.liyyao.rabbitmq.restaurant.entity.po.Product;
import org.apache.ibatis.annotations.Select;

/**
 * 商品Dao
 */
public interface ProductDao {

    @Select("SELECT id, name, price, restaurant_id, status, date " +
            "FROM product " +
            "WHERE id=#{id}")
    Product selectProduct(String id);
}
