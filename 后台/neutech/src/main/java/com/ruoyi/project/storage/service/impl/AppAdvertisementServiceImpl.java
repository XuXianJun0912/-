package com.ruoyi.project.storage.service.impl;

import com.ruoyi.common.constant.PointsWayEnum;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.storage.domain.AdvertisementVO;
import com.ruoyi.project.storage.domain.ParamsAdvertisementVO;
import com.ruoyi.project.storage.mapper.AppAdvertisementMapper;
import com.ruoyi.project.storage.service.AppAdvertisementService;
import com.ruoyi.project.storage.service.AppPointRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AppAdvertisementServiceImpl implements AppAdvertisementService {

    @Resource
    private AppAdvertisementMapper appAdvertisementMapper;

    @Resource
    private AppPointRecordService appPointRecordService;

    @Override
    public AjaxResult getList() {
        List<AdvertisementVO> list = appAdvertisementMapper.getList();
        return AjaxResult.success(list);
    }

    @Override
    public AjaxResult getAdverInfo(Long id) {
        AdvertisementVO advertisementVO = appAdvertisementMapper.getAdverInfo(id);
        if(advertisementVO != null) {
            return AjaxResult.success(advertisementVO);
        }
        return AjaxResult.error("没有查询到指定id的广告信息");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult setPoints(ParamsAdvertisementVO paramsAdvertisementVO) {
        /**
         * 校验paramsAdvertisementVO不能为空
         */
        if (null == paramsAdvertisementVO){
            return AjaxResult.error("广告积分参数不能为空");
        }

        AdvertisementVO advertisementVO = appAdvertisementMapper.getAdverInfo(paramsAdvertisementVO.getId());
        if (advertisementVO == null){
            return AjaxResult.error("该广告不存在");
        }

        /**
         * 意外获取积分
         */
        if (paramsAdvertisementVO.getPoints().longValue() != advertisementVO.getPoints()){
            return AjaxResult.error("禁止非法获取积分");
        }

        /**
         * 个人总积分
         */
        paramsAdvertisementVO.setUserId(SecurityUtils.getUserId());
        paramsAdvertisementVO.setUpdateBy(SecurityUtils.getUsername());
        Integer nums = appAdvertisementMapper.setPoints(paramsAdvertisementVO);

        if (nums != 1){
            throw new CustomException("个人总积分增加失败");
        }

        //积分记录 @TODO
        paramsAdvertisementVO.setPoints(advertisementVO.getPoints().longValue());
        Integer pointNum = appPointRecordService.savePoint(SecurityUtils.getUsername(),SecurityUtils.getUserId(),PointsWayEnum.AD,paramsAdvertisementVO.getPoints(),paramsAdvertisementVO.getId(),null);
        if (1 != pointNum){
            throw new CustomException("插入积分异常");
        }
        return AjaxResult.success();
    }
}
