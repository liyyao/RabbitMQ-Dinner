package com.liyyao.rabbitmq.transmessage.sender;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liyyao.rabbitmq.transmessage.entity.po.TransMessage;
import com.liyyao.rabbitmq.transmessage.service.TransMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 消息发送调用方法
 */
@Component
@Slf4j
public class TransMessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private TransMessageService transMessageService;

    public void send(String serviceName, String exchange, String routingKey, Message payload) {
        log.info("消息发送（）：exchange:{} routingKey:{} payload:{}", exchange, routingKey, payload);
        try {
            ObjectMapper mapper = new ObjectMapper();
            String payloadStr = mapper.writeValueAsString(payload);
            String messageId = payload.getMessageProperties().getMessageId();
            TransMessage transMessage = transMessageService.messageSendReady(serviceName, messageId, exchange, routingKey, payloadStr);//消息入库
            rabbitTemplate.convertAndSend(exchange, routingKey, payload, new CorrelationData(transMessage.getId()));    //发送消息到下一服务
            log.info("message sent, ID:{}", transMessage.getId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
