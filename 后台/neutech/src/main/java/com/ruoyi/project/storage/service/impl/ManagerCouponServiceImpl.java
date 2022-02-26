package com.ruoyi.project.storage.service.impl;

import com.ruoyi.project.storage.domain.CouponVo;
import com.ruoyi.project.storage.domain.CustomerVO;
import com.ruoyi.project.storage.mapper.ManagerCouponMapper;
import com.ruoyi.project.storage.service.ManagerCouponService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
@Slf4j
public class ManagerCouponServiceImpl implements ManagerCouponService {

    @Resource
    private ManagerCouponMapper managerCouponMapper;

    @Override
    public Integer saveCoupon(CustomerVO customerVO) {

        CouponVo couponVo = new CouponVo();
        couponVo.setUserId(customerVO.getUserId());
        couponVo.setOrderId(null);
        couponVo.setCreateBy(customerVO.getUserName());
        couponVo.setCreateTime(new Date());
        couponVo.setDelFlag(0);
        couponVo.setWay(0);
        couponVo.setPoints((long) 100);
        Integer nums = managerCouponMapper.saveCoupon(couponVo);
        if (1 != nums){
            log.error("保存优惠券失败：{}",customerVO);
        }

        return nums;
    }
}
