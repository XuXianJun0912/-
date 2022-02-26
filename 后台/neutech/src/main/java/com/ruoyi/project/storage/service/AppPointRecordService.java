package com.ruoyi.project.storage.service;

import com.ruoyi.common.constant.PointsWayEnum;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.storage.domain.PointRecordVO;

import java.util.List;

public interface AppPointRecordService {
    List<PointRecordVO> getList();

    Integer savePoint(String userName,Long userId,PointsWayEnum way, Long points, Long advertisementId, Long orderId );

    AjaxResult selectPoint();

}
