package com.ruoyi.project.storage.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.project.common.enums.MsgCodeEnums;
import com.ruoyi.project.common.util.ActiveMqConfig;
import com.ruoyi.project.storage.domain.BackendOrderVO;
import com.ruoyi.project.storage.domain.CouponVo;
import com.ruoyi.project.storage.domain.CustomerVO;
import com.ruoyi.project.storage.domain.MsgInfo;
import com.ruoyi.project.storage.service.ManagerCouponService;
import com.ruoyi.project.storage.service.QueueConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class QueueConsumerImpl implements QueueConsumer {

    @Resource
    private ManagerCouponService managerCouponService;

    @Override
    @JmsListener(destination = ActiveMqConfig.QUEUE_NAME)
    public void receiveQueueMsg(String msg) {
        log.info("收到的消息为：" + msg);
        if (null != msg){
            JSONObject msgInfo = JSON.parseObject(msg);
            if (msgInfo.getString("code").equals(MsgCodeEnums.REG.getCode())){
                /**
                 * 注册服务
                 * 给注册的用户优惠券
                 */
                CustomerVO customerVO =  msgInfo.getObject("object",CustomerVO.class);
                log.info("发送优惠券:{},{}" , customerVO.getUserName() , customerVO.getUserId());

                Integer nums = managerCouponService.saveCoupon(customerVO);

            }else if (msgInfo.getString("code").equals(MsgCodeEnums.ORDER.getCode())){
                /**
                 * 订单服务
                 * 发送短信
                 */
                BackendOrderVO backendOrderVO = msgInfo.getObject("object",BackendOrderVO.class);
                log.info("发送短信:{}" , backendOrderVO.getEmptyBoxCallPhone()+"******");

            }
        }
    }
}
