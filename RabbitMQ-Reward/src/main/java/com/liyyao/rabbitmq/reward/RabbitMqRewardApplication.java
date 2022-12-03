package com.liyyao.rabbitmq.reward;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.liyyao.rabbitmq")
@SpringBootApplication
public class RabbitMqRewardApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqRewardApplication.class, args);
    }

}
