package com.liyyao.rabbitmq.deliveryman.consumer;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.liyyao.rabbitmq.deliveryman.config.RabbitConfig;
import com.liyyao.rabbitmq.deliveryman.dao.DeliverymanDao;
import com.liyyao.rabbitmq.deliveryman.entity.dto.OrderMessageDTO;
import com.liyyao.rabbitmq.deliveryman.entity.po.Deliveryman;
import com.liyyao.rabbitmq.deliveryman.enums.DeliverymanStatus;
import com.liyyao.rabbitmq.deliveryman.enums.OrderStatus;
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
import java.util.List;


/**
 * 餐厅监听器
 */
@Component
@Slf4j
public class DeliverymanConsumer {

    @Autowired
    private DeliverymanDao deliverymanDao;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 处理订单
     * @param message
     * @param channel
     */
    @RabbitListener(queues = RabbitConfig.DELIVERYMAN_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public void orderMessage(Message message, Channel channel) throws IOException {
        OrderMessageDTO orderMessageDTO = JSON.parseObject( new String(message.getBody()), OrderMessageDTO.class);
        List<Deliveryman> deliverymanList = deliverymanDao.selectAvailableDeliveryman(DeliverymanStatus.AVAILABLE); //获取目前所有空闲状态的骑手
        if (CollectionUtil.isNotEmpty(deliverymanList)) {
            orderMessageDTO.setDeliverymanId(deliverymanList.get(0).getId());
            orderMessageDTO.setOrderStatus(OrderStatus.DELIVERYMAN_CONFIRMED);      //更新订单状态为骑手已送达
            log.info("订单已分配骑手，当前消息：{}", orderMessageDTO);

            //发送消息
            MessageProperties properties = new MessageProperties();
            properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            properties.setContentEncoding("utf-8");
            String jsonMsg = JSON.toJSONString(orderMessageDTO);
            Message sendMsg = new Message(jsonMsg.getBytes(StandardCharsets.UTF_8), properties);

            CorrelationData correlationData = new CorrelationData();
            ReturnedMessage returnedMessage = new ReturnedMessage(sendMsg, 300, "骑手已送达订单，发送消息给订单服务队列", RabbitConfig.DELIVERYMAN_EXCHANGE, "key.order");
            correlationData.setReturned(returnedMessage);
            correlationData.setId(orderMessageDTO.getOrderId());    //设置为订单ID
            rabbitTemplate.convertAndSend(RabbitConfig.DELIVERYMAN_EXCHANGE, "key.order", sendMsg, correlationData);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);   //手动消息应答
        } else {
            log.info("订单暂无可分配的骑手，请耐心等待，当前消息状态：{}", orderMessageDTO);
        }

    }
}
