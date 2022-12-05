package com.liyyao.rabbitmq.transmessage.entity.po;

import com.liyyao.rabbitmq.transmessage.enums.TransMessageType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 交换机获取失败的消息落库实体类
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TransMessage {

    private String id;      //消息id
    private String service; //消息发送的服务
    private TransMessageType type;   //状态枚举类型
    private String exchange;   //交换机
    private String routingKey;  //路由key
    private String queue;       //队列
    private Integer sequence;   //发送次数
    private String payload;     //Message的JSON
    private Date date;      //时间
}
