package com.ruoyi.project.storage.service.impl;

import com.ruoyi.project.storage.service.MsgProducer;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;

@Service
public class MsgProducerImpl implements MsgProducer {
    @Resource
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Override
    public void sendMessage(Destination destination, String msg) {

        jmsMessagingTemplate.convertAndSend(destination, msg);
    }
}
