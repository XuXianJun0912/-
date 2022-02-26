package com.ruoyi.project.storage.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.common.enums.MsgCodeEnums;
import com.ruoyi.project.common.util.RedisCon;
import com.ruoyi.project.common.util.SeqGeneratorUtil;
import com.ruoyi.project.storage.domain.*;
import com.ruoyi.project.storage.mapper.*;
import com.ruoyi.project.storage.service.AppBoxStandardService;
import com.ruoyi.project.storage.service.MsgProducer;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.jms.Destination;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class AppBoxStandardServiceImpl implements AppBoxStandardService {

    @Resource
    private AppBoxStandardMapper appBoxStandardMapper;

    @Resource
    private AppAddressMapper appAddressMapper;

    @Resource
    private ManagerBoxStandardMapper managerBoxStandardMapper;

    @Resource
    private ManagerBoxInfoMapper managerBoxInfoMapper;

    @Resource
    private ManagerCustomerMapper managerCustomerMapper;

    @Resource
    private AppPointRecordMapper appPointRecordMapper;

    @Resource
    private ManagerOrderMapper managerOrderMapper;

    @Resource
    private RedisCache redisCache;

    @Resource
    private MsgProducer producer;

    @Resource
    private Destination queue;

    @Override
    public AjaxResult getBoxDefaultAddress() {
        List<AddressVO> list = appAddressMapper.getAddressDefault(SecurityUtils.getUserId());
        if (0 == list.size()){
            return AjaxResult.error("未找到");
        }
        return AjaxResult.success("查询成功",list);
    }

    @Override
    public AjaxResult selectBoxStandard() {
        List<BoxStandardSelectVO> list = appBoxStandardMapper.selectBoxStandard();
        if (0 == list.size()){
            return AjaxResult.error("未找到");
        }
        return AjaxResult.success("查找成功",list);
    }

    @Override
    public AjaxResult orderBox(OrderEmptyBoxParamsVO orderEmptyBoxParamsVO) {
        /**
         * 校验参数
         */
        if (null == orderEmptyBoxParamsVO){
            throw new CustomException("参数不能为空");
        }
        if (StringUtils.isEmpty(orderEmptyBoxParamsVO.getOrderName())){
            throw new CustomException("订单名称不能为空");
        }
        if (StringUtils.isEmpty(orderEmptyBoxParamsVO.getEmptyBoxCallName())){
            throw new CustomException("接受人姓名不能为空");
        }
        if (!StringUtils.isNotEmpty(orderEmptyBoxParamsVO.getEmptyBoxCallAddress())){
            throw new CustomException("接受人地址不能为空");
        }
        if (!StringUtils.isNotEmpty(orderEmptyBoxParamsVO.getEmptyBoxCallPhone())){
            throw new CustomException("接收人手机号不能为空");
        }
        /**
         * 空箱上门时间，空箱上门时段
         */
        if (StringUtils.isEmpty(orderEmptyBoxParamsVO.getBoxStandard())){
            throw new CustomException("箱子规格不能为空");
        }
        if ( null == orderEmptyBoxParamsVO.getBoxUnitPrice() ){
            throw new CustomException("箱子单价不能为空");
        }
        if (null == orderEmptyBoxParamsVO.getLeaseDuration()){
            throw new CustomException("租赁时长最少一个月");
        }
        if (null == orderEmptyBoxParamsVO.getTotalPrice()){
            throw new CustomException("总积分不能为空");
        }
        if (orderEmptyBoxParamsVO.getBoxUnitPrice() * orderEmptyBoxParamsVO.getLeaseDuration() != orderEmptyBoxParamsVO.getTotalPrice()){
            throw new CustomException("总积分输入错误，请重新计算");
        }
        /**
         * 选用一个未占用的箱子,校验积分是否够买箱子,更新当前积分。插入积分记录，插入订单信息，插入订单历史信息
         */

        //箱子单价的校验
        BoxStandardVO boxStandardVO = managerBoxStandardMapper.findBoxStandard(orderEmptyBoxParamsVO.getBoxStandard());
        if (!boxStandardVO.getBoxUnitPrice().equals(orderEmptyBoxParamsVO.getBoxUnitPrice())){
            throw new CustomException(orderEmptyBoxParamsVO.getBoxStandard() +"的规格的箱子单价错误");
        }

        //校验用户拥有的积分是否足够支付订单所需积分
        Long price = orderEmptyBoxParamsVO.getBoxUnitPrice();
        Integer time = orderEmptyBoxParamsVO.getLeaseDuration();
        Long totalPrice = price * time;
        if (!totalPrice.equals(orderEmptyBoxParamsVO.getTotalPrice())){
            throw new CustomException("总积分输入错误，操作失败");
        }
        List<CustomerVO> customerVOList = managerCustomerMapper.getList(SecurityUtils.getUsername());
        if ( null == customerVOList || 0 == customerVOList.size()){
            throw new CustomException("未查询到登录客户的信息");
        }
        if (totalPrice > customerVOList.get(0).getCurrentPoints() ){
            throw new CustomException("客户积分不足");
        }

        //查询可用箱子的数量
        BoxInfoVO boxInfoVO = new BoxInfoVO();
        boxInfoVO.setIsUsed(0);
        boxInfoVO.setBoxStandard(orderEmptyBoxParamsVO.getBoxStandard());
        List<BoxInfoVO> list = managerBoxInfoMapper.getList(boxInfoVO);
        if (list == null || 0 == list.size()){
            throw new CustomException( orderEmptyBoxParamsVO.getBoxStandard() + "没有可用的箱子");
        }

        BoxInfoVO boxInfoVO1 = null;
        for (BoxInfoVO boxItem : list
             ) {

            /**
             * 更新占用
             */
            Long userId = SecurityUtils.getUserId();
            Integer boxNum = appBoxStandardMapper.updateUsedBox(userId,boxItem.getId(),boxItem.getVersion());
            if (boxNum == 0){
                continue;
            }
            boxInfoVO1 = boxItem;
            break;
        }

        /**
         * 校验是否更新成功
         */
        if (null == boxInfoVO1){
            throw new CustomException("没有可以使用的箱子");
        }

        /**
         * 更新当前积分
         */
        Integer pointNum = appBoxStandardMapper.updateCurPoints(totalPrice,SecurityUtils.getUsername());
        if (1 != pointNum){
            throw new CustomException("积分更新失败");
        }

        /**
         * 插入订单信息
         */
        Long orderCode = Long.valueOf(SeqGeneratorUtil.seqGenerator(DateUtils.getNowDateStr(),6));
        BackendOrderVO backendOrderVO = new BackendOrderVO();
        backendOrderVO.setUserId(SecurityUtils.getUserId());
        backendOrderVO.setStatus(1);
        backendOrderVO.setOrderCode(orderCode);
        backendOrderVO.setOrderName(orderEmptyBoxParamsVO.getOrderName());
        backendOrderVO.setEmptyBoxOrderTime(orderEmptyBoxParamsVO.getEmptyBoxOrderTime());
        backendOrderVO.setEmptyBoxCallName(orderEmptyBoxParamsVO.getEmptyBoxCallName());
        backendOrderVO.setEmptyBoxCallAddress(orderEmptyBoxParamsVO.getEmptyBoxCallAddress());
        backendOrderVO.setEmptyBoxCallPhone(orderEmptyBoxParamsVO.getEmptyBoxCallPhone());
        backendOrderVO.setEmptyBoxCallTime(orderEmptyBoxParamsVO.getEmptyBoxCallTime());
        backendOrderVO.setEmptyBoxCallInterval(orderEmptyBoxParamsVO.getEmptyBoxCallInterval());
        backendOrderVO.setBoxId(boxInfoVO1.getId());
        backendOrderVO.setBoxCode(boxInfoVO1.getBoxCode());
        backendOrderVO.setBoxStandard(boxInfoVO1.getBoxStandard());
        backendOrderVO.setBoxUnitPrice(boxInfoVO1.getBoxUnitPrice());
        backendOrderVO.setLeaseDuration(orderEmptyBoxParamsVO.getLeaseDuration());
        backendOrderVO.setTotalPrice(totalPrice);
        backendOrderVO.setCreateBy(SecurityUtils.getUsername());
        Integer emptyBoxOrderNum = appBoxStandardMapper.insertEmptyBoxOrder(backendOrderVO);
        if (1 != emptyBoxOrderNum){
            throw new CustomException("插入订单信息失败");
        }

        /**
         * 查询刚才插入的订单
         */
        BackendOrderVO backendOrderVO2 = appBoxStandardMapper.selectOrderByCode(orderCode);

        /**
         * 保存到redis
         */
        ValueOperations<String,BackendOrderVO> redisOpt = redisCache.setCacheObject(RedisCon.ORDER_PR + backendOrderVO2.getId()+"",backendOrderVO2);
        if (null == redisOpt){
            return AjaxResult.error("订单信息保存到缓存异常");
        }

        /**
         * 插入积分记录
         */
        PointRecordVO pointRecordVO = new PointRecordVO();
        pointRecordVO.setWay(3);
        pointRecordVO.setUserId(SecurityUtils.getUserId());
        pointRecordVO.setPoints(totalPrice);
        pointRecordVO.setCreateBy(SecurityUtils.getUsername());
        pointRecordVO.setUpdateBy(SecurityUtils.getUsername());
        pointRecordVO.setAdvertisementId(null);
        pointRecordVO.setSortNo(null);
        //查找orderId
        Long orderId = appBoxStandardMapper.selectOrder(orderCode);
        if (null == orderId){
            throw new CustomException("订单不存在");
        }
        pointRecordVO.setOrderId(orderId);
        Integer savePointNum = appPointRecordMapper.savePoint(pointRecordVO);
        if (1 != savePointNum){
            throw new CustomException("插入积分记录失败");
        }

        /**
         * 插入订单历史信息
         */
        BackendOrderVO backendOrderVO1 = new BackendOrderVO();
        backendOrderVO1.setId(orderId);
        Integer numOrderHis = managerOrderMapper.saveOrderHis(backendOrderVO1);
        if (numOrderHis != 1) {
            throw new CustomException("保存订单历史表异常，请联系管理员");
        }

        /**
         * 发送mq信息
         */
        MsgInfo msgInfo = new MsgInfo();
        msgInfo.setCode(MsgCodeEnums.ORDER.getCode());
        msgInfo.setObject(backendOrderVO2);
        producer.sendMessage(queue, JSON.toJSONString(msgInfo));

        return AjaxResult.success("预约成功");
    }
}
