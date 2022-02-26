package com.ruoyi.project.common.util;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Destination;

/*** activemq的配置 * @author shengwu ni */
@Configuration
public class ActiveMqConfig {
    /*** 点对点模式队列名称 */
    public static final String QUEUE_NAME = "activemq.queue";

    @Bean
    public Destination queue() {
        return new ActiveMQQueue(QUEUE_NAME);
    }
}