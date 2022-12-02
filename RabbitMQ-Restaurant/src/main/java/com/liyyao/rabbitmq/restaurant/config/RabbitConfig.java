package com.liyyao.rabbitmq.restaurant.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String RESTAURANT_EXCHANGE = "exchange.order.restaurant";
    public static final String RESTAURANT_QUEUE = "queue.restaurant";
    public static final String RESTAURANT_ROUTING_KEY = "key.restaurant";

    @Autowired
    private RabbitProperties properties;

    /**
     * RabbitMQ连接池，从配置文件中读取参数
     * @return
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(properties.getHost());
        factory.setPort(properties.getPort());
        factory.setUsername(properties.getUsername());
        factory.setPassword(properties.getPassword());
        factory.setVirtualHost(properties.getVirtualHost());
        factory.setPublisherReturns(properties.isPublisherReturns());   //开启连接池的ReturnCallBack支持
        factory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);   //开启连接池的Publisher-confirm-type支持
        return factory;
    }

    /**
     * RabbitListener使用连接池，使用连接池时 spring.rabbitmq.listener.simple配置不生效
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL); //关闭自动ACK
        factory.setConnectionFactory(connectionFactory);    //使用连接池
        factory.setPrefetchCount(1);    //设置QOS
        factory.setMessageConverter(new Jackson2JsonMessageConverter());    //设置消息转换器为JSON
        return factory;
    }

    /**
     * 创建餐厅交换机
     */
    @Bean
    public DirectExchange restaurantExchange() {
        return ExchangeBuilder.directExchange(RESTAURANT_EXCHANGE).durable(true).build();
    }

    /**
     * 创建订单队列
     * @return
     */
    @Bean
    public Queue restaurantQueue() {
        return QueueBuilder.durable(RESTAURANT_QUEUE).build();
    }

    /**
     * 绑定关系创建
     * @param exchange
     * @param queue
     * @return
     */
    @Bean
    public Binding restaurantBind(@Qualifier("restaurantExchange") DirectExchange exchange,
                                  @Qualifier("restaurantQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(RESTAURANT_ROUTING_KEY);
    }
}
