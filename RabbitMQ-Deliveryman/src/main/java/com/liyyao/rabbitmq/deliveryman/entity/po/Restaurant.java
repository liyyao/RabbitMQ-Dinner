package com.liyyao.rabbitmq.deliveryman.entity.po;

import com.liyyao.rabbitmq.restaurant.enums.RestaurantStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 餐厅落库实体类
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Restaurant {

    private String id;          //餐厅id
    private String name;        //名称
    private String address;     //地址
    private RestaurantStatus status;    //状态
    private String settltmentId;    //结算id
    private Date date;          //时间
}
