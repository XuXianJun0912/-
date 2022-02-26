package com.ruoyi.project.storage.mapper;

import com.ruoyi.project.storage.domain.BackendOrderVO;
import com.ruoyi.project.storage.domain.BoxStandardSelectVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppBoxStandardMapper {

    List<BoxStandardSelectVO> selectBoxStandard();

    Integer updateUsedBox(@Param("userId") Long userId, @Param("id") Long id, @Param("version") Long version);

    Integer updateCurPoints(@Param("totalPrice") Long totalPrice, @Param("username") String username);

    Integer insertEmptyBoxOrder(BackendOrderVO backendOrderVO);


    Long selectOrder(Long orderCode);

    BackendOrderVO selectOrderByCode(Long orderCode);
}
