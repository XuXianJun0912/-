package com.ruoyi.project.storage.service;

import javax.jms.Destination;

public interface MsgProducer {
    void sendMessage(Destination destination, String msg);
}
