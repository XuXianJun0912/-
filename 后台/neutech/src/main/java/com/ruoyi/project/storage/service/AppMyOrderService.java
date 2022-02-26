package com.ruoyi.project.storage.service;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.storage.domain.BackendOrderVO;


import java.util.List;

public interface AppMyOrderService {
    List<BackendOrderVO> getList();

    AjaxResult operateOrder(Long id, Integer operate, Long version, BackendOrderVO backendOrderVO);

    AjaxResult getOrderInfo(Long id);

}
