package com.ruoyi.project.storage.mapper;

import com.ruoyi.project.storage.domain.BackendOrderVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AppMyOrderMapper {

    List<BackendOrderVO> getList(Long userId);

    BackendOrderVO getOrderInfo(@Param(value = "id") Long id , @Param(value = "userId") Long userId);

    Integer updateOrder(Map<String, Object> map);

    Integer deleteOrder(Map<String, Object> map);

}
