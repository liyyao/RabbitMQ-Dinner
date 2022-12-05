package com.liyyao.rabbitmq.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = {"com.liyyao.rabbitmq.order", "com.liyyao.rabbitmq.transmessage.dao"})
@SpringBootApplication(scanBasePackages = "com.liyyao.rabbitmq")
public class RabbitMqOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqOrderApplication.class, args);
    }

}
