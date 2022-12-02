package com.liyyao.rabbitmq.restaurant.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


/**
 * 自定义实现不可路由消息兜底方法
 */
@Slf4j
@Component
public class MyBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ConnectionFactory connectionFactory;

    /**
     * 将实现类注入
     *  注入顺序：Constructor(构造方法) -> @Autowired(依赖注入) -> @PostConstruct(注解的方法)
     */
    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
        rabbitTemplate.setConnectionFactory(connectionFactory);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData.getId() != null ? correlationData.getId() : "";
        if (!ack) {
            log.warn("{}交换机接收ID为{}的消息失败，原因：{}", correlationData.getReturned().getExchange(), id, cause);
        }
    }

    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.warn("不可路由消息相应码：" + returnedMessage.getReplyCode());
        log.warn("不可路由消息主体：" + returnedMessage.getMessage());
        log.warn("不可路由消息描述：" + returnedMessage.getReplyText());
        log.warn("不可路由消息使用的交换机 exchange：" + returnedMessage.getExchange());
        log.warn("不可路由消息使用的路由键 routing key：" + returnedMessage.getRoutingKey());
    }
}
