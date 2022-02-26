package com.ruoyi.project.storage.service;

public interface QueueConsumer {
    void receiveQueueMsg(String msg);
}
