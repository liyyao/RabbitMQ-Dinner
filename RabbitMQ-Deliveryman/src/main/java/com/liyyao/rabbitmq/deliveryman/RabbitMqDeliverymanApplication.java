package com.liyyao.rabbitmq.deliveryman;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.liyyao.rabbitmq")
@SpringBootApplication
public class RabbitMqDeliverymanApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqDeliverymanApplication.class, args);
    }

}
