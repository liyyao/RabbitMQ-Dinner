package com.liyyao.rabbitmq.restaurant;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan
@SpringBootApplication
public class RabbitMqRestaurantApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqRestaurantApplication.class, args);
    }

}
