package com.liyyao.rabbitmq.order.service;

import com.alibaba.fastjson.JSON;
import com.liyyao.rabbitmq.order.config.RabbitConfig;
import com.liyyao.rabbitmq.order.dao.OrderDetailDao;
import com.liyyao.rabbitmq.order.entity.dto.OrderMessageDTO;
import com.liyyao.rabbitmq.order.entity.po.OrderDetail;
import com.liyyao.rabbitmq.order.entity.vo.OrderCreateVo;
import com.liyyao.rabbitmq.order.enums.OrderStatus;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

/**
 * 订单请求Service
 */
@Service
public class OrderService {
    @Autowired
    private OrderDetailDao orderDetailDao;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 创建订单
     * @param orderCreateVo
     */
    public void createOrder(OrderCreateVo orderCreateVo) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setAddress(orderCreateVo.getAddress());
        orderDetail.setAccountId(orderCreateVo.getAccountId());
        orderDetail.setProductId(orderCreateVo.getProductId());
        orderDetail.setStatus(OrderStatus.ORDER_CREATING);
        orderDetail.setDate(new Date());
        orderDetail.setId(UUID.randomUUID().toString());
        orderDetailDao.save(orderDetail);

        OrderMessageDTO messageDTO = new OrderMessageDTO();
        messageDTO.setOrderId(orderDetail.getId());
        messageDTO.setProductId(orderDetail.getProductId());
        messageDTO.setAccountId(orderDetail.getAccountId());

        String json = JSON.toJSONString(messageDTO);
        MessageProperties properties = new MessageProperties();
        properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        Message message = new Message(json.getBytes(StandardCharsets.UTF_8), properties);   //指定为JSON格式消息
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(orderDetail.getId());     //设置为订单ID，保证全局唯一
        ReturnedMessage returnedMessage = new ReturnedMessage(message, 100, "用户创建订单消息", RabbitConfig.RESTAURANT_EXCHANGE, RabbitConfig.ORDER_ROUTING_KEY);
        correlationData.setReturned(returnedMessage);   //设置不可路由捕获的消息

        rabbitTemplate.convertAndSend(RabbitConfig.RESTAURANT_EXCHANGE, "key.restaurant", message, correlationData);
    }
}
