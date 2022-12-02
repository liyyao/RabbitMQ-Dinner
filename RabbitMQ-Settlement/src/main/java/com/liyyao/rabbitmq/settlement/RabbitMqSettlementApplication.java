package com.liyyao.rabbitmq.settlement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.liyyao.rabbitmq")
@SpringBootApplication
public class RabbitMqSettlementApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqSettlementApplication.class, args);
    }

}
