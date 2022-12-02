package com.liyyao.rabbitmq.order.consumer;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.liyyao.rabbitmq.order.config.RabbitConfig;
import com.liyyao.rabbitmq.order.dao.OrderDetailDao;
import com.liyyao.rabbitmq.order.entity.dto.OrderMessageDTO;
import com.liyyao.rabbitmq.order.entity.po.OrderDetail;
import com.liyyao.rabbitmq.order.enums.OrderStatus;
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

/**
 * 订单消息监听器
 */
@Component
@Slf4j
public class OrderConsumer {

    @Autowired
    private OrderDetailDao orderDetailDao;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitConfig.ORDER_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public void order(Message message, Channel channel) throws IOException {
        OrderMessageDTO messageDTO = JSON.parseObject(new String(message.getBody()), OrderMessageDTO.class);    //获取订单消息
        OrderDetail orderDetail = orderDetailDao.selectOrder(messageDTO.getOrderId());  //获取订单入库数据

        //创建发送消息
        Message deliveryMsg = null;
        MessageProperties properties = new MessageProperties();
        properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        CorrelationData correlationData = new CorrelationData();
        switch (messageDTO.getOrderStatus()) {
            //餐厅已确认
            case RESTAURANT_CONFIRMED:
                //判断是否创建订单且有价格，没有认为订单创建失败
                if (messageDTO.getConfirmed() && messageDTO.getPrice() != null) {
                    //更新数据库的订单
                    orderDetail.setStatus(OrderStatus.RESTAURANT_CONFIRMED);
                    orderDetail.setPrice(messageDTO.getPrice());
                    orderDetailDao.update(orderDetail);

                    //给骑手微服务发送消息
                    String json = JSON.toJSONString(messageDTO);
                    deliveryMsg = new Message(json.getBytes(StandardCharsets.UTF_8), properties);
                    ReturnedMessage returnedMessage = new ReturnedMessage(deliveryMsg, 200, "餐厅已确认订单，消息发送骑手服务", RabbitConfig.DELIVERYMAN_EXCHANGE, "key.deliveryman");
                    correlationData.setReturned(returnedMessage);
                    correlationData.setId(messageDTO.getOrderId());
                    rabbitTemplate.convertAndSend(RabbitConfig.DELIVERYMAN_EXCHANGE, "key.deliveryman", deliveryMsg, correlationData);
                    log.info("已更新数据库订单状态，给骑手服务发送消息");
                } else {
                    orderDetail.setStatus(OrderStatus.FAILED);
                    orderDetailDao.update(orderDetail);
                }
                break;
            //骑手送达确认
            case DELIVERYMAN_CONFIRMED:
                if (StrUtil.isNotBlank(messageDTO.getDeliverymanId())) {
                    orderDetail.setStatus(OrderStatus.DELIVERYMAN_CONFIRMED);
                    orderDetail.setDeliverymanId(messageDTO.getDeliverymanId());
                    orderDetailDao.update(orderDetail);
                    //给结算服务发送消息
                    String json = JSON.toJSONString(messageDTO);
                    deliveryMsg = new Message(json.getBytes(StandardCharsets.UTF_8), properties);
                    ReturnedMessage returnedMessage = new ReturnedMessage(deliveryMsg, 200, "骑手已确认送达，消息发送给结算服务", RabbitConfig.SETTLEMENT_EXCHANGE, "key.settlement");
                    correlationData.setReturned(returnedMessage);
                    correlationData.setId(messageDTO.getOrderId());
                    rabbitTemplate.convertAndSend(RabbitConfig.SETTLEMENT_EXCHANGE, "key.settlement", deliveryMsg, correlationData);
                    log.info("已更新数据库订单状态，给结算服务发送消息！");
                } else {
                    orderDetail.setStatus(OrderStatus.FAILED);
                    orderDetailDao.update(orderDetail);
                }
                break;
            //订单结算确认
            case SETTLEMENT_CONFIRMED:
                if (StrUtil.isNotBlank(messageDTO.getSettlementId())) {
                    //更新数据的订单
                    orderDetail.setStatus(OrderStatus.SETTLEMENT_CONFIRMED);
                    orderDetail.setSettlementId(messageDTO.getSettlementId());
                    orderDetailDao.update(orderDetail);
                    //给奖励服务发送消息
                    String json = JSON.toJSONString(messageDTO);
                    deliveryMsg = new Message(json.getBytes(StandardCharsets.UTF_8), properties);
                    ReturnedMessage returnedMessage = new ReturnedMessage(deliveryMsg, 200, "用户订单已确认结算，消息发送给订单奖励服务", RabbitConfig.REWARD_EXCHANGE, "key.reward");
                    correlationData.setReturned(returnedMessage);
                    correlationData.setId(messageDTO.getOrderId());
                    rabbitTemplate.convertAndSend(RabbitConfig.REWARD_EXCHANGE, "key.reward", deliveryMsg, correlationData);

                    log.info("已更新数据库订单状态，给订单奖励服务发送消息！");
                } else {
                    orderDetail.setStatus(OrderStatus.FAILED);
                    orderDetailDao.update(orderDetail);
                }
                break;
            //订单结束
            case ORDER_CREATED:
                if (StrUtil.isNotBlank(messageDTO.getRewardId())) {
                    //更新数据库的订单
                    orderDetail.setStatus(OrderStatus.ORDER_CREATED);
                    orderDetail.setRewardId(messageDTO.getRewardId());
                    orderDetailDao.update(orderDetail);
                } else {
                    orderDetail.setStatus(OrderStatus.FAILED);
                    orderDetailDao.update(orderDetail);
                }
                break;
            default: break;
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
