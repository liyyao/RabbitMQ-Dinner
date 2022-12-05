package com.liyyao.rabbitmq.transmessage.service.impl;

import com.liyyao.rabbitmq.transmessage.dao.TransMessageDao;
import com.liyyao.rabbitmq.transmessage.entity.po.TransMessage;
import com.liyyao.rabbitmq.transmessage.enums.TransMessageType;
import com.liyyao.rabbitmq.transmessage.service.TransMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 不可路由消息处理Service
 */
@Service
public class TransMessageServiceImpl implements TransMessageService {

    @Autowired
    TransMessageDao transMessageDao;

    @Override
    public TransMessage messageSendReady(String serviceName, String id, String exchange, String routingKey, String body) {
        TransMessage transMessage = new TransMessage();
        transMessage.setId(id);
        transMessage.setService(serviceName);
        transMessage.setExchange(exchange);
        transMessage.setRoutingKey(routingKey);
        transMessage.setPayload(body);
        transMessage.setDate(new Date());
        transMessage.setSequence(0);
        transMessage.setType(TransMessageType.SEND);
        transMessageDao.insert(transMessage);
        return transMessage;
    }

    @Override
    public void messageSendSuccess(String id, String serviceName) {
        transMessageDao.delete(id, serviceName);
    }

    @Override
    public void messageSendSuccess(String id) {
        transMessageDao.deleteById(id);
    }

    @Override
    public TransMessage messageSendReturn(String id, String exchange, String routingKey, String body, String serviceName) {
        return messageSendReady(serviceName, id, exchange, routingKey, body);
    }

    @Override
    public List<TransMessage> listReadyMessage() {
        System.out.println("testst............................");
        return transMessageDao.selectByType(TransMessageType.SEND.toString());
    }

    /**
     * 在修改不可路由消息状态时，需要使用分布式锁进行保障，保证同一时间只有一个任务可以进行状态，这里进行省略
     * @param id
     */
    @Override
    public void messageResend(String id, String serviceName) {
        TransMessage transMessage = transMessageDao.selectByIdAndService(id, serviceName);
        transMessage.setSequence(transMessage.getSequence() + 1);
        transMessageDao.update(transMessage);
    }

    /**
     * 在修改不可路由消息状态时，需要使用分布式锁进行保障，保证同一时间只有一个任务可以进行状态，这里进行省略
     * @param id
     */
    @Override
    public void MessageDead(String id, String serviceName) {
        TransMessage transMessage = transMessageDao.selectByIdAndService(id, serviceName);
        transMessage.setType(TransMessageType.DEAD);
        transMessageDao.update(transMessage);
    }
}
