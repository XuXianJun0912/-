package com.ruoyi.project.storage.service.impl;

import com.ruoyi.common.constant.PointsWayEnum;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.storage.domain.PointRecordVO;
import com.ruoyi.project.storage.mapper.AppPointRecordMapper;
import com.ruoyi.project.storage.service.AppPointRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AppPointRecordServiceImpl implements AppPointRecordService {

    @Resource
    private AppPointRecordMapper appPointRecordMapper;

    @Override
    public List<PointRecordVO> getList() {
        List<PointRecordVO> list = appPointRecordMapper.getList(SecurityUtils.getUserId());
        return list;
    }

    @Override
    public Integer savePoint(String userName,Long userId,PointsWayEnum way, Long points, Long advertisementId, Long orderId) {
        PointRecordVO pointRecordVO = new PointRecordVO();
        pointRecordVO.setCreateBy(userName);
        pointRecordVO.setUpdateBy(userName);
        pointRecordVO.setUserId(userId);
        pointRecordVO.setAdvertisementId(advertisementId);
        pointRecordVO.setPoints(points);
        pointRecordVO.setOrderId(orderId);
        pointRecordVO.setWay(way.getWay());
        Integer nums = appPointRecordMapper.savePoint(pointRecordVO);
        return nums;
    }

    @Override
    public AjaxResult selectPoint() {
        Long nums = appPointRecordMapper.selectPoint(SecurityUtils.getUsername());
        if (null == nums){
            return AjaxResult.error("查询失败");
        }
        return AjaxResult.success("查询成功",nums);
    }
}
