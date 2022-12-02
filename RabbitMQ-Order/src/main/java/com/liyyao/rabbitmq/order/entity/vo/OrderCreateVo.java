package com.liyyao.rabbitmq.order.entity.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 订单实体类
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class OrderCreateVo {

    private String accountId;       //用户ID
    private String address;         //地址
    private String productId;       //产品ID
}
