package com.liyyao.rabbitmq.transmessage.service;

import com.liyyao.rabbitmq.transmessage.entity.po.TransMessage;

import java.util.List;

public interface TransMessageService {

    /**
     * 发送前暂存消息
     * @param id
     * @param exchange
     * @param routingKey
     * @param body
     * @return
     */
    TransMessage messageSendReady(String serviceName, String id, String exchange, String routingKey, String body);

    /**
     * 设置消息发送成功
     * @param id
     */
    void messageSendSuccess(String id, String serviceName);

    /**
     * 设置消息发送成功
     * @param id
     */
    void messageSendSuccess(String id);

    /**
     * 设置消息返回
     * @param id
     * @param exchange
     * @param routingKey
     * @param body
     * @return
     */
    TransMessage messageSendReturn(String id, String exchange, String routingKey, String body, String serviceName);

    /**
     * 查询应发未发消息
     * @return
     */
    List<TransMessage> listReadyMessage();

    /**
     * 记录消息发送次数
     * @param id
     */
    void messageResend(String id, String serviceName);

    /**
     * 消息重发多次，放弃
     * @param id
     */
    void MessageDead(String id, String serviceName);
}
