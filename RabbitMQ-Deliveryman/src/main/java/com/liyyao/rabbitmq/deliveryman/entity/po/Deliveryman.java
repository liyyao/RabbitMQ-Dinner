package com.liyyao.rabbitmq.deliveryman.entity.po;

import com.liyyao.rabbitmq.deliveryman.enums.DeliverymanStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 骑手落库实体类
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Deliveryman {

    private String id;      //骑手id
    private String name;    //名称
    private String district;    //骑手区域
    private DeliverymanStatus status;   //状态
    private Date date;      //时间
}
