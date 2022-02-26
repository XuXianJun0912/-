package com.ruoyi.project.storage.mapper;

import com.ruoyi.project.storage.domain.BackendOrderVO;
import com.ruoyi.project.storage.domain.GetOrderListParamsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ManagerOrderMapper {
    List<BackendOrderVO> getOrderList(GetOrderListParamsVO getOrderListParamsVO);

    BackendOrderVO getOrderById(@Param(value = "id") Long id);

    Integer deleteOrder(@Param("ids") Long[] ids);

    Integer saveOrderHis(BackendOrderVO backendOrderVO);

    Integer updateOrder(Map<String, Object> map);
}
