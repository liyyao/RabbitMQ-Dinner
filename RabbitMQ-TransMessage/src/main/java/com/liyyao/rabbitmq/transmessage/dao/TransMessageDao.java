package com.liyyao.rabbitmq.transmessage.dao;

import com.liyyao.rabbitmq.transmessage.entity.po.TransMessage;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 不可路由消息Dao
 */
public interface TransMessageDao {

    @Insert("INSERT INTO trans_message (id, type, service, exchange, " +
            "routingkey, queue, sequence, payload, date) " +
            "VALUES (#{id}, #{type}, #{service}, #{exchange}, #{routingKey}, #{queue}, " +
            "#{sequence}, #{payload}, #{date})")
    void insert(TransMessage transMessage);

    @Update("UPDATE trans_message set type=#{type}, service=#{service}, exchange=#{exchange}, " +
            "routingkey=#{routingKey}, queue=#{queue}, sequence=#{sequence}, payload=#{payload}, " +
            "date=#{date} " +
            "WHERE id=#{id} and service=#{service}")
    void update(TransMessage transMessage);

    @Select("SELECT id, type, service, exchange, routingkey, queue, sequence, payload, date " +
            "FROM trans_message " +
            "WHERE id=#{id} and service=#{service}")
    TransMessage selectByIdAndService(@Param("id") String id, @Param("service") String service);

    @Select("SELECT id, type, service, exchange, routingkey, queue, sequence, payload, date " +
            "FROM trans_message " +
            "WHERE type=#{type} and service=#{service}")
    List<TransMessage> selectByTypeAndService(@Param("type") String type, @Param("service") String service);

    @Select("SELECT id, type, service, exchange, routingkey, queue, sequence, payload, date " +
            "FROM trans_message " +
            "WHERE type=#{type}")
    List<TransMessage> selectByType(@Param("type") String type);

    @Delete("DELETE FROM trans_message " +
            "WHERE id=#{id} and service=#{service}")
    void delete(@Param("id") String id, @Param("service") String service);

    @Delete("DELETE FROM trans_message " +
            "WHERE id=#{id}")
    void deleteById(@Param("id") String id);
}
