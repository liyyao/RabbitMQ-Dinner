package com.liyyao.rabbitmq.reward.consumer;

import com.alibaba.fastjson.JSON;
import com.liyyao.rabbitmq.reward.config.RabbitConfig;
import com.liyyao.rabbitmq.reward.dao.RewardDao;
import com.liyyao.rabbitmq.reward.entity.dto.OrderMessageDTO;
import com.liyyao.rabbitmq.reward.entity.po.Reward;
import com.liyyao.rabbitmq.reward.enums.OrderStatus;
import com.liyyao.rabbitmq.reward.enums.RewardStatus;
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
public class RewardConsumer {

    @Autowired
    private RewardDao rewardDao;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 处理订单
     * @param message
     * @param channel
     */
    @RabbitListener(queues = RabbitConfig.REWARD_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public void orderMessage(Message message, Channel channel) throws IOException {
        OrderMessageDTO orderMessageDTO = JSON.parseObject( new String(message.getBody()), OrderMessageDTO.class);
        log.info("奖励服务获取订单消息：{}", orderMessageDTO);
        //奖励落库
        String rewardId = UUID.randomUUID().toString();
        Reward reward = new Reward();
        reward.setId(rewardId);
        reward.setOrderId(orderMessageDTO.getOrderId());
        reward.setStatus(RewardStatus.SUCCESS);
        reward.setAmount(orderMessageDTO.getPrice());
        reward.setDate(new Date());
        rewardDao.insert(reward);

        orderMessageDTO.setOrderStatus(OrderStatus.ORDER_CREATED);    //更新订单状态为骑手已送达
        orderMessageDTO.setRewardId(reward.getId());

        //发送消息
        MessageProperties properties = new MessageProperties();
        properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        properties.setContentEncoding("utf-8");
        String jsonMsg = JSON.toJSONString(orderMessageDTO);
        Message sendMsg = new Message(jsonMsg.getBytes(StandardCharsets.UTF_8), properties);

        CorrelationData correlationData = new CorrelationData();
        ReturnedMessage returnedMessage = new ReturnedMessage(sendMsg, 300, "奖励收到订单消息，发送消息给订单服务队列", RabbitConfig.REWARD_EXCHANGE, "key.order");
        correlationData.setReturned(returnedMessage);
        correlationData.setId(orderMessageDTO.getOrderId());    //设置为订单ID
        rabbitTemplate.convertAndSend(RabbitConfig.REWARD_EXCHANGE, "key.order", sendMsg, correlationData);

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);   //手动消息应答

    }
}
