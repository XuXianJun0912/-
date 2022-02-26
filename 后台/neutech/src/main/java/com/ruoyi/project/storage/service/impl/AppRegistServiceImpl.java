package com.ruoyi.project.storage.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.constant.PointsWayEnum;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.common.enums.MsgCodeEnums;
import com.ruoyi.project.storage.domain.CustomerVO;
import com.ruoyi.project.storage.domain.MsgInfo;
import com.ruoyi.project.storage.mapper.AppRegistMapper;
import com.ruoyi.project.storage.mapper.ManagerCustomerMapper;
import com.ruoyi.project.storage.service.AppPointRecordService;
import com.ruoyi.project.storage.service.AppRegistService;
import com.ruoyi.project.storage.service.MsgProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.jms.Destination;

@Service
public class AppRegistServiceImpl implements AppRegistService {

    @Value("${default.points:1000}")
    private String points;

    @Resource
    private AppRegistMapper appRegistMapper;

    @Resource
    private ManagerCustomerMapper managerCustomerMapper;

    @Resource
    private AppPointRecordService appPointRecordService;

    @Resource
    private MsgProducer producer;

    @Resource
    private Destination queue;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult registUser(CustomerVO customerVO) {

        if (null == customerVO) {
            return AjaxResult.error("用户信息不能为空");
        }

        if (StringUtils.isEmpty(customerVO.getUserName())) {
            return AjaxResult.error("用户名不能为空");
        } else {
            if (customerVO.getUserName().length() > 16) {
                throw new CustomException("最大长度为16");
            }
        }

        if (StringUtils.isEmpty(customerVO.getPassword())) {
            return AjaxResult.error("密码不能为空");
        }

        if (StringUtils.isEmpty(customerVO.getNickName())) {
            return AjaxResult.error("姓名不能为空");
        } else {
            if (customerVO.getNickName().length() > 10) {
                throw new CustomException("最大长度为10");
            }
        }

        if (StringUtils.isEmpty(customerVO.getPhonenumber())) {
            return AjaxResult.error("手机号不能为空");
        } else {
            if (customerVO.getPhonenumber().length() > 11) {
                throw new CustomException("最大长度为11");
            }
        }

        if (StringUtils.isEmpty(customerVO.getEmail())) {
            return AjaxResult.error("邮箱不能为空");
        }

        if (StringUtils.isEmpty(customerVO.getSex())) {
            if (!("0".equals(customerVO.getSex()) || "1".equals(customerVO.getSex()))) {
                throw new CustomException("性别只能是0：男，1：女");
            } else {
                throw new CustomException("性别不能为空");
            }
        }

//        账号唯一性
        int countUserName = managerCustomerMapper.findCustomerByUserName(customerVO.getUserName());
        if (countUserName > 0) {
            return AjaxResult.error("用户已存在");
        }

//        手机号唯一性
        int countPhone = managerCustomerMapper.findCustomerByPhone(customerVO.getPhonenumber());
        if (countPhone > 0) {
            return AjaxResult.error("手机号已存在");
        }

//        邮箱唯一性
        int countEmail = managerCustomerMapper.findCustomerByEm(customerVO.getEmail());
        if (countEmail > 0) {
            return AjaxResult.error("邮箱已存在");
        }


        customerVO.setPassword(SecurityUtils.encryptPassword(customerVO.getPassword()));
        customerVO.setCurrentPoints(Long.valueOf(points));

        Integer nums = appRegistMapper.registAppUser(customerVO);

        if (1 == nums) {
            //插入积分@TODO
            /**
             * 通过查询新插入的用户获取用户的id
             */
            CustomerVO customerVO1 = appRegistMapper.findCustomer(customerVO.getUserName());
            if (null != customerVO1) {
                Integer pointNum = appPointRecordService.savePoint(customerVO.getUserName(), customerVO1.getUserId(), PointsWayEnum.REGIST, Long.valueOf(points), null, null);
                if (1 != pointNum) {
                    throw new CustomException("插入积分异常");
                }
            }

            MsgInfo msgInfo = new MsgInfo();
            msgInfo.setObject(customerVO1);
            msgInfo.setCode(MsgCodeEnums.REG.getCode());
            producer.sendMessage(queue, JSON.toJSONString(msgInfo));
            return AjaxResult.success();
        }
        return AjaxResult.error("注册失败");
    }
}
