package com.liyyao.rabbitmq.transmessage;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.liyyao.rabbitmq.transmessage.dao")
@SpringBootApplication
public class RabbitMqTransMessageApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitMqTransMessageApplication.class, args);
    }

}
