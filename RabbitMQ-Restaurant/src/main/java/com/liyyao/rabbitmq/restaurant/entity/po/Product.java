package com.liyyao.rabbitmq.restaurant.entity.po;

import cn.hutool.db.DaoTemplate;
import com.liyyao.rabbitmq.restaurant.enums.ProductStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品落库实体类
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Product {

    private String id;      //产品id
    private String name;    //名称
    private BigDecimal price;   //价格
    private String restaurantId;    //餐厅
    private ProductStatus status;   //状态
    private Date date;      //时间
}
