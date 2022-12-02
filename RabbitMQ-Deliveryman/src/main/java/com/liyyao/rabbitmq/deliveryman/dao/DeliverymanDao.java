package com.liyyao.rabbitmq.deliveryman.dao;

import com.liyyao.rabbitmq.deliveryman.entity.po.Deliveryman;
import com.liyyao.rabbitmq.deliveryman.enums.DeliverymanStatus;
import com.liyyao.rabbitmq.restaurant.entity.po.Product;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 商品Dao
 */
public interface DeliverymanDao {

    @Select("SELECT id, name, status, date " +
            "FROM deliveryman " +
            "WHERE id=#{id}")
    Deliveryman selectDeliveryman(String id);

    @Select("SELECT id, name, status, date " +
            "FROM deliveryman " +
            "WHERE status=#{status}")
    List<Deliveryman> selectAvailableDeliveryman(DeliverymanStatus status);
}
