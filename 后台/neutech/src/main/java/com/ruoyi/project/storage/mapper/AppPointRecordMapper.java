package com.ruoyi.project.storage.mapper;

import com.ruoyi.project.storage.domain.PointRecordVO;

import java.util.List;

public interface AppPointRecordMapper {
    List<PointRecordVO> getList(Long userId);

    Integer savePoint(PointRecordVO pointRecordVO);

    Long selectPoint(String username);
}
