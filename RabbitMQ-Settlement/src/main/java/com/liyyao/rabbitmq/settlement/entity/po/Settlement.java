package com.liyyao.rabbitmq.settlement.entity.po;

import com.liyyao.rabbitmq.settlement.enums.SettlementStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 结算落库实体类
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Settlement {

    private String id;      //骑手id
    private String orderId; //订单ID
    private String transactionId;   //交易ID
    private SettlementStatus status;   //状态
    private BigDecimal amount;  //金额
    private Date date;      //时间
}
