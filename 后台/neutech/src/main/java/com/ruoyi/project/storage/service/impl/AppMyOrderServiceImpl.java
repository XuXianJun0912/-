package com.ruoyi.project.storage.service.impl;

import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.common.enums.OrderOperateEnum;
import com.ruoyi.project.common.util.RedisCon;
import com.ruoyi.project.storage.domain.BackendOrderVO;
import com.ruoyi.project.storage.mapper.AppMyOrderMapper;
import com.ruoyi.project.storage.mapper.ManagerOrderMapper;
import com.ruoyi.project.storage.service.AppMyOrderService;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class AppMyOrderServiceImpl implements AppMyOrderService {

    @Resource
    private AppMyOrderMapper appMyOrderMapper;

    @Resource
    private ManagerOrderMapper managerOrderMapper;

    @Resource
    private RedisCache redisCache;

    @Override
    public List<BackendOrderVO> getList() {
        List<BackendOrderVO> list = appMyOrderMapper.getList(SecurityUtils.getUserId());
        return list;
    }

    @Override
    public AjaxResult getOrderInfo(Long id) {
        if (id == null){
            return AjaxResult.error("id值不能为空");
        }

        /**
         * 先查询缓存，查询不到在查询数据库
         */
        BackendOrderVO backendOrderVORedis = redisCache.getCacheObject(RedisCon.ORDER_PR + id);
        BackendOrderVO backendOrderVO;
        if (backendOrderVORedis == null){
            backendOrderVO = appMyOrderMapper.getOrderInfo(id,SecurityUtils.getUserId());
            if (null == backendOrderVO ){
                return AjaxResult.error("查询失败");
            }else {
                /**
                 * 将查询到数据写入redis缓存
                 */
                ValueOperations<String,BackendOrderVO> redisOpt = redisCache.setCacheObject(RedisCon.ORDER_PR + id + "",backendOrderVO);
                if (null == redisOpt){
                    return AjaxResult.error("写入redis缓存异常");
                }
                return AjaxResult.success("查询成功",backendOrderVO);
            }
        }
        return AjaxResult.success("查询成功",backendOrderVORedis);
    }

    @Override
    public AjaxResult operateOrder(Long id, Integer operate, Long version, BackendOrderVO backendOrderVO ) {
        if (id == null){
            return AjaxResult.error("参数为空");
        }
        /**
         * 参数校验
         */
        BackendOrderVO backendOrderVO1 = managerOrderMapper.getOrderById(id);
        if (null == backendOrderVO1) {
            return AjaxResult.error("订单不存在");
        }

        /**
         * 保存订单历史表
         */
        Integer nums = managerOrderMapper.saveOrderHis(backendOrderVO1);
        if (nums != 1) {
            throw new CustomException("保存订单历史表异常，请联系管理员");
        }

        /**
         * 3：【收取空箱】；
         * 4：【发送重箱】；
         * 6：【预约提取】；
         * 8：【收取重箱】
         * 9：【发送空箱】
         * 10：【删除订单】
         */
        if (    OrderOperateEnum.APP_GET_EMPTY.getOperate() == operate.longValue() ||
                OrderOperateEnum.APP_SEND_HEAVY.getOperate() == operate.longValue() ||
                OrderOperateEnum.APP_GET_HEAVY.getOperate() == operate.longValue() ||
                OrderOperateEnum.APP_SEND_EMPTY.getOperate() == operate.longValue()
        ){
            /**
             * 更新订单表
             */
            Map<String, Object> map = new HashMap<>();
            map.put("status", operate);
            map.put("id", id);
            map.put("version", version);
            map.put("updateBy", SecurityUtils.getUsername());
            map.put("updateTime", new Date());
            Integer orderNum = managerOrderMapper.updateOrder(map);
            if (orderNum != 1) {
                throw new CustomException("更新箱子信息异常。");
            }
        }

        /**
         * 重箱提取
         */
        if (OrderOperateEnum.APP_ORDER_GET.getOperate() == operate.longValue()){
            if (
                    StringUtils.isEmpty(backendOrderVO.getHeavyBoxCallAddress()) ||
                    StringUtils.isEmpty(backendOrderVO.getHeavyBoxCallInterval()) ||
                    StringUtils.isEmpty(backendOrderVO.getHeavyBoxCallName()) ||
                    StringUtils.isEmpty(backendOrderVO.getHeavyBoxCallPhone()) ||
                    null == backendOrderVO.getHeavyBoxCallTime() ||
                    null == backendOrderVO.getHeavyBoxOrderTime()
            ){
                throw new CustomException("物流信息不能为空");
            }
            /**
             * 更新订单表
             */
            Map<String, Object> map = new HashMap<>();
            map.put("status", operate);
            map.put("id", id);
            map.put("version", version);
            map.put("updateBy", SecurityUtils.getUsername());
            map.put("heavyBoxCallName" , backendOrderVO.getHeavyBoxCallName());
            map.put("heavyBoxCallPhone" , backendOrderVO.getHeavyBoxCallPhone());
            map.put("heavyBoxCallAddress" , backendOrderVO.getHeavyBoxCallAddress());
            map.put("heavyBoxCallTime" , backendOrderVO.getHeavyBoxCallTime());
            map.put("heavyBoxCallInterval" , backendOrderVO.getHeavyBoxCallInterval());
            map.put("heavyBoxOrderTime" , backendOrderVO.getHeavyBoxOrderTime());
            Integer orderNum = appMyOrderMapper.updateOrder(map);
            if (orderNum != 1) {
                throw new CustomException("更新箱子信息异常！");
            }
        }

        /**
         * 删除订单
         */
        if (OrderOperateEnum.APP_DEL_ORDER.getOperate() == operate.longValue()){
            /**
             * 未完成的订单不支持删除
             */
            if (backendOrderVO1.getStatus() != OrderOperateEnum.APP_DEL_ORDER.getOperate().longValue()){
                throw new CustomException("订单未完成，不支持删除");
            }

            Map<String, Object> map = new HashMap<>();
            map.put("status", operate);
            map.put("id", id);
            map.put("version", version);
            map.put("updateBy", SecurityUtils.getUsername());
            map.put("appDelFlag" , 2);
            Integer orderNum = appMyOrderMapper.deleteOrder(map);
            if (orderNum != 1) {
                throw new CustomException("删除箱子信息异常");
            }
        }

        /**
         * 更新redis缓存
         */
        BackendOrderVO backendOrderVO2 = appMyOrderMapper.getOrderInfo(id,SecurityUtils.getUserId());
        ValueOperations<String,BackendOrderVO> redisOpt = redisCache.setCacheObject(RedisCon.ORDER_PR + id + "",backendOrderVO2);
        if (null == redisOpt){
            return AjaxResult.error("写入redis缓存异常");
        }

        return AjaxResult.success();
    }
}
