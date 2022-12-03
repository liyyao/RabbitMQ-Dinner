package com.liyyao.rabbitmq.reward.entity.po;

import com.liyyao.rabbitmq.reward.enums.RewardStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 奖励落库实体类
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Reward {

    private String id;      //奖励id
    private String orderId; //订单ID
    private BigDecimal amount;  //金额
    private RewardStatus status;   //状态
    private Date date;      //时间
}
