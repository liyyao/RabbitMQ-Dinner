package com.liyyao.rabbitmq.reward.entity.dto;

import com.liyyao.rabbitmq.reward.enums.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 订单消息实体类
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class OrderMessageDTO {

    private String orderId;             //订单ID
    private OrderStatus orderStatus;    //订单状态枚举值
    private String address;             //订单地址
    private String deliverymanId;       //骑手ID
    private String productId;           //产品ID
    private String accountId;           //用户ID
    private String settlementId;        //结算ID
    private String rewardId;            //积分结算ID
    private BigDecimal rewardAmount;    //积分奖励数量
    private BigDecimal price;           //价格
    private Boolean confirmed;          //确认
}
