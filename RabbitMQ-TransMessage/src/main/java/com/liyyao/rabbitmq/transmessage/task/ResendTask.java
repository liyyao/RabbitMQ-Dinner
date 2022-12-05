package com.liyyao.rabbitmq.transmessage.task;

import com.liyyao.rabbitmq.transmessage.entity.po.TransMessage;
import com.liyyao.rabbitmq.transmessage.service.TransMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时处理发送失败消息
 */
@EnableScheduling
@Configuration
@Component
@Slf4j
public class ResendTask {

    @Autowired
    private TransMessageService transMessageService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Scheduled(fixedDelayString = "10000")
    public void resendMessage() {
        log.info("定时消息服务启动");
        List<TransMessage> transMessages = transMessageService.listReadyMessage();
        for (TransMessage message : transMessages) {
            log.info("resendMessage(): message:{}", message);
            if (message.getSequence() > 5) {
                log.error("resend too many times!");
                continue;
            }
            MessageProperties properties = new MessageProperties();
            properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            properties.setMessageId(message.getId());
            Message sendMessage = new Message(message.getPayload().getBytes(), properties);
            rabbitTemplate.convertAndSend(message.getExchange(), message.getRoutingKey(), sendMessage, new CorrelationData(message.getId()));
            log.info("message sent, ID:{}", message.getId());
            transMessageService.messageResend(message.getId(), message.getService());
        }
    }
}
