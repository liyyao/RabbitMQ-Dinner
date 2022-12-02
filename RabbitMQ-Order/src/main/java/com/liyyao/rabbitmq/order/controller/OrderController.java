package com.liyyao.rabbitmq.order.controller;

import com.liyyao.rabbitmq.order.entity.vo.OrderCreateVo;
import com.liyyao.rabbitmq.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/orders")
    public void createOrder(@RequestBody OrderCreateVo orderCreateVo) {
        log.info("创建订单：orderCreateVo:{}" + orderCreateVo);
        orderService.createOrder(orderCreateVo);
    }
}
