package com.ruoyi.project.storage.service;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.storage.domain.BackendOrderVO;
import com.ruoyi.project.storage.domain.GetOrderListParamsVO;

import java.util.List;

public interface ManagerOrderService {
    List<BackendOrderVO> getOrderList(GetOrderListParamsVO getOrderListParamsVO);

    AjaxResult getOrderById(Long id);

    AjaxResult deleteOrder(Long[] ids);

    AjaxResult operateOrder(Long id, Integer operate, Long version, BackendOrderVO backendOrderVO);
}
