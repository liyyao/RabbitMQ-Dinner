package com.liyyao.rabbitmq.restaurant.consumer;

import com.alibaba.fastjson.JSON;
import com.liyyao.rabbitmq.restaurant.config.RabbitConfig;
import com.liyyao.rabbitmq.restaurant.dao.ProductDao;
import com.liyyao.rabbitmq.restaurant.dao.RestaurantDao;
import com.liyyao.rabbitmq.restaurant.entity.dto.OrderMessageDTO;
import com.liyyao.rabbitmq.restaurant.entity.po.Product;
import com.liyyao.rabbitmq.restaurant.entity.po.Restaurant;
import com.liyyao.rabbitmq.restaurant.enums.OrderStatus;
import com.liyyao.rabbitmq.restaurant.enums.ProductStatus;
import com.liyyao.rabbitmq.restaurant.enums.RestaurantStatus;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Correlation;
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
 * 餐厅监听器
 */
@Component
@Slf4j
public class RestaurantConsumer {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private RestaurantDao restaurantDao;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 处理订单
     * @param message
     * @param channel
     */
    @RabbitListener(queues = RabbitConfig.RESTAURANT_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public void orderMessage(Message message, Channel channel) throws IOException {
        String bodyStr = new String(message.getBody());
        OrderMessageDTO orderMessageDTO = JSON.parseObject(bodyStr, OrderMessageDTO.class);
        Product product = productDao.selectProduct(orderMessageDTO.getProductId());     //获取数据库中的商品数据
        if (product == null) {
            log.warn("there isn't a product by id :" + orderMessageDTO.getProductId());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);   //手动消息应答
            return;
        }
        Restaurant restaurant = restaurantDao.selectRestaurant(product.getRestaurantId());//通过商品获取对应的餐厅数据

        //判断当前店家是否已经打烊且订单对应商品库存是否充足，更新订单状态
        if (restaurant.getStatus() == RestaurantStatus.OPEN && product.getStatus() == ProductStatus.AVAILABLE) {
            orderMessageDTO.setConfirmed(true);
            orderMessageDTO.setOrderStatus(OrderStatus.RESTAURANT_CONFIRMED);   //餐厅已确认
            orderMessageDTO.setPrice(product.getPrice());
        } else {
            orderMessageDTO.setOrderStatus(OrderStatus.FAILED); //订单创建失败
            orderMessageDTO.setConfirmed(false);
        }

        //发送消息
        MessageProperties properties = new MessageProperties();
        properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        properties.setContentEncoding("utf-8");
        String jsonMsg = JSON.toJSONString(orderMessageDTO);
        Message sendMsg = new Message(jsonMsg.getBytes(StandardCharsets.UTF_8), properties);
        CorrelationData correlationData = new CorrelationData();
        ReturnedMessage returnedMessage = new ReturnedMessage(sendMsg, 300, "餐厅收到订单消息，发送给订单服务队列", RabbitConfig.RESTAURANT_EXCHANGE, "key.order");
        correlationData.setReturned(returnedMessage);
        correlationData.setId(orderMessageDTO.getOrderId());    //设置为订单ID
        rabbitTemplate.convertAndSend(RabbitConfig.RESTAURANT_EXCHANGE, "key.order", sendMsg, correlationData);

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);   //手动消息应答
        log.info("餐厅收到消息，已确认...");
    }
}
