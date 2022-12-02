package com.liyyao.rabbitmq.order.entity.po;

import com.liyyao.rabbitmq.order.enums.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单落库实体类
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class OrderDetail {
    private String id;                  //订单ID
    private OrderStatus status;         //订单状态枚举值
    private String address;             //订单地址
    private String accountId;           //用户ID
    private String productId;           //产品ID
    private String deliverymanId;       //骑手ID
    private String settlementId;        //结算ID
    private String rewardId;            //积分结算ID
    private BigDecimal price;           //价格
    private LocalDateTime date;         //时间
}
