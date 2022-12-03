package com.liyyao.rabbitmq.settlement.consumer;

import com.alibaba.fastjson.JSON;
import com.liyyao.rabbitmq.settlement.config.RabbitConfig;
import com.liyyao.rabbitmq.settlement.dao.SettlementDao;
import com.liyyao.rabbitmq.settlement.entity.dto.OrderMessageDTO;
import com.liyyao.rabbitmq.settlement.entity.po.Settlement;
import com.liyyao.rabbitmq.settlement.enums.OrderStatus;
import com.liyyao.rabbitmq.settlement.enums.SettlementStatus;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;


/**
 * 餐厅监听器
 */
@Component
@Slf4j
public class SettlementConsumer {

    @Autowired
    private SettlementDao settlementDao;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 处理订单
     * @param message
     * @param channel
     */
    @RabbitListener(queues = RabbitConfig.SETTLEMTNT_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public void orderMessage(Message message, Channel channel) throws IOException {
        OrderMessageDTO orderMessageDTO = JSON.parseObject( new String(message.getBody()), OrderMessageDTO.class);
        log.info("结算服务获取订单消息：{}", orderMessageDTO);
        //结算落库
        String settlementId = UUID.randomUUID().toString();
        Settlement settlement = new Settlement();
        settlement.setId(settlementId);
        settlement.setAmount(orderMessageDTO.getPrice());
        settlement.setDate(new Date());
        settlement.setStatus(SettlementStatus.SUCCESS);
        settlement.setTransactionId(UUID.randomUUID().toString());
        settlementDao.insert(settlement);

        orderMessageDTO.setOrderStatus(OrderStatus.SETTLEMENT_CONFIRMED);    //更新订单状态为骑手已送达
        orderMessageDTO.setSettlementId(settlementId);

        //发送消息
        MessageProperties properties = new MessageProperties();
        properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        properties.setContentEncoding("utf-8");
        String jsonMsg = JSON.toJSONString(orderMessageDTO);
        Message sendMsg = new Message(jsonMsg.getBytes(StandardCharsets.UTF_8), properties);

        CorrelationData correlationData = new CorrelationData();
        ReturnedMessage returnedMessage = new ReturnedMessage(sendMsg, 300, "结算收到订单消息，发送消息给订单服务队列", RabbitConfig.SETTLEMTNT_EXCHANGE, "key.order");
        correlationData.setReturned(returnedMessage);
        correlationData.setId(orderMessageDTO.getOrderId());    //设置为订单ID
        rabbitTemplate.convertAndSend(RabbitConfig.SETTLEMTNT_EXCHANGE, "key.order", sendMsg, correlationData);

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);   //手动消息应答

    }
}
