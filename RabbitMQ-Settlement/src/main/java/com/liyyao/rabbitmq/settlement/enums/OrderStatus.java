package com.liyyao.rabbitmq.settlement.enums;

/**
 * 订单状态枚举类
 */
public enum OrderStatus {

    ORDER_CREATING,    //订单创建中
    RESTAURANT_CONFIRMED,   //餐厅已确认
    DELIVERYMAN_CONFIRMED,  //骑手送达确认
    SETTLEMENT_CONFIRMED,   //已结算
    ORDER_CREATED,      //订单已完成
    FAILED;             //订单结束
}
