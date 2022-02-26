package com.ruoyi.project.storage.service.impl;

import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.common.util.RedisCon;
import com.ruoyi.project.storage.domain.BackendOrderVO;
import com.ruoyi.project.storage.domain.GetOrderListParamsVO;
import com.ruoyi.project.storage.mapper.AppMyOrderMapper;
import com.ruoyi.project.storage.mapper.ManagerBoxInfoMapper;
import com.ruoyi.project.storage.mapper.ManagerOrderMapper;
import com.ruoyi.project.storage.service.ManagerOrderService;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ManagerOrderServiceImpl implements ManagerOrderService {

    @Resource
    private ManagerOrderMapper managerOrderMapper;

    @Resource
    private ManagerBoxInfoMapper managerBoxInfoMapper;

    @Resource
    private AppMyOrderMapper appMyOrderMapper;

    @Resource
    private RedisCache redisCache;

    @Override
    public List<BackendOrderVO> getOrderList(GetOrderListParamsVO getOrderListParamsVO) {
        List<BackendOrderVO> list = managerOrderMapper.getOrderList(getOrderListParamsVO);
        return list;
    }

    @Override
    public AjaxResult getOrderById(Long id) {

        if (id == null){
            return AjaxResult.error("id值不能为空");
        }

        BackendOrderVO backendOrderVO = managerOrderMapper.getOrderById(id);
        if (null == backendOrderVO){
            return AjaxResult.error("查找不到此id对应的订单");
        }
        return AjaxResult.success(backendOrderVO);
    }

    @Override
    public AjaxResult deleteOrder(Long[] ids) {

        if (ids == null || 0 == ids.length){
            return AjaxResult.error("id不能为空");
        }

        /**
         * 删除前先查询是否存在id对应的订单
         * @TODO
         */

        Integer nums = managerOrderMapper.deleteOrder(ids);
        if (nums <= 0){
            return AjaxResult.error("删除失败");
        }
        return AjaxResult.success(nums);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult operateOrder(Long id, Integer operate, Long version, BackendOrderVO backendOrderVO) {

        /**
         * 参数校验
         */
        BackendOrderVO backendOrderVO1 = managerOrderMapper.getOrderById(id);
        BackendOrderVO backendOrderVO2 = managerOrderMapper.getOrderById(backendOrderVO.getId());
        if (null == backendOrderVO1 && null == backendOrderVO2) {
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
         * 发送空箱
         * 收取重箱
         * 发送重箱
         */
        if ("2".equals(operate.toString()) || "5".equals(operate.toString()) || "7".equals(operate.toString())) {

            /**
             * 更新订单表
             */
            Map<String, Object> map = new HashMap<>();
            map.put("status", operate);
            map.put("id", id);
            map.put("version", version);
            map.put("updateBy", SecurityUtils.getUsername());
            Integer orderNum = managerOrderMapper.updateOrder(map);
            if (orderNum != 1) {
                throw new CustomException("更新箱子信息异常");
            }
        }
        /**
         *收取空箱
         */
        if ("10".equals(operate.toString())) {
            /**
             * 更新订单表
             */
            Long boxId = backendOrderVO1.getBoxId();
            Integer numBox = managerBoxInfoMapper.freeOrderBox(boxId);
            if (numBox == 1) {
                /**
                 * 收取空箱订单已完成
                 */
                Map<String, Object> map = new HashMap<>();
                map.put("status", operate);
                map.put("id", id);
                map.put("version", version);
                map.put("updateBy", SecurityUtils.getUsername());
                Integer orderNum = managerOrderMapper.updateOrder(map);
                if (orderNum != 1) {
                    throw new CustomException("收取空箱异常");
                }
            }else {
                throw new CustomException("释放箱子异常");
            }
        }

        /**
         * 更新redis缓存
         */
        BackendOrderVO backendOrderVO3 = appMyOrderMapper.getOrderInfo(id,SecurityUtils.getUserId());
        ValueOperations<String,BackendOrderVO> redisOpt = redisCache.setCacheObject(RedisCon.ORDER_PR + id + "",backendOrderVO3);
        if (null == redisOpt){
            return AjaxResult.error("写入redis缓存异常");
        }
        return AjaxResult.success();
    }
}
