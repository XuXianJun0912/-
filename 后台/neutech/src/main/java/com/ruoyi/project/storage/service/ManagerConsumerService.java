package com.ruoyi.project.storage.service;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.storage.domain.ConsumerVO;

import java.util.List;

public interface ManagerConsumerService {
    List<ConsumerVO> getList(ConsumerVO consumerVO);

    AjaxResult createConsumer(ConsumerVO consumerVO);

    AjaxResult updateConsumer(ConsumerVO consumerVO);

    AjaxResult deleteConsumer(Long[] userIds);

    AjaxResult resetPassword(Long[] userIds);

    AjaxResult enableConsumer(String operate, Long[] userIds);
}
