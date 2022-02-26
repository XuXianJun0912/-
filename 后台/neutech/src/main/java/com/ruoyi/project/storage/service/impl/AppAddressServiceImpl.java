package com.ruoyi.project.storage.service.impl;

import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.storage.domain.AddressVO;
import com.ruoyi.project.storage.mapper.AppAddressMapper;
import com.ruoyi.project.storage.service.AppAddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class AppAddressServiceImpl implements AppAddressService {

    @Resource
    private AppAddressMapper appAddressMapper;

    /**
     * 获取地址列表
     * @return
     */
    @Override
    public List<AddressVO> getList() {

        Long userId = SecurityUtils.getUserId();

        List<AddressVO> list = appAddressMapper.getList(userId);

        return list;
    }

    /**
     * 获取默认地址
     * @return
     */
    @Override
    public AjaxResult getAddressDefault() {
        Long userId = SecurityUtils.getUserId();
        List<AddressVO> addressVO = appAddressMapper.getAddressDefault(userId);
        if (addressVO.size() == 0){
            return AjaxResult.error("地址获取失败");
        }
        /**
         * 如果数据库中出现多个默认地址，数据一定出错了，可以用以下方法避免
         */
        return AjaxResult.success("地址获取成功",addressVO.get(0));
    }

    /**
     * 地址新增
     * @param addressVO
     * @return
     */
    @Override
    public AjaxResult createAddress(AddressVO addressVO) {

        if (null == addressVO){
            return AjaxResult.error("地址不能为空");
        }

        if (
            StringUtils.isEmpty(addressVO.getAddress()) ||
            StringUtils.isEmpty(addressVO.getContacts()) ||
            StringUtils.isEmpty(addressVO.getContactsPhone())
        ){
            return AjaxResult.error("地址信息不能为空");
        }

        /**
         * 查询数据库有没有默认:如果有默认地址，设置为非默认
         */
        selectDefaultAddress(addressVO);

        addressVO.setUpdateBy(SecurityUtils.getUsername());
        addressVO.setCreateBy(SecurityUtils.getUsername());
        addressVO.setUserId(SecurityUtils.getUserId());
        Integer nums = appAddressMapper.createAddress(addressVO);
        if (1 != nums ){
            throw new CustomException("地址新增失败");
        }
        return AjaxResult.success("地址新增成功");
    }

    /**
     * 地址删除
     * @param id
     * @return
     */
    @Override
    public AjaxResult deleteAddress(Long id) {
        Integer nums = appAddressMapper.deleteAddress(id);
        if (1 != nums){
            return AjaxResult.error("删除失败");
        }
        return AjaxResult.success("删除成功");
    }

    @Override
    public AjaxResult updateAddress(@RequestBody AddressVO addressVO) {

        if (null == addressVO){
            return AjaxResult.error("地址不能为空");
        }

        if (
                StringUtils.isEmpty(addressVO.getAddress()) ||
                StringUtils.isEmpty(addressVO.getContacts()) ||
                StringUtils.isEmpty(addressVO.getContactsPhone()) ||
                StringUtils.isEmpty(String.valueOf(addressVO.getId()))
        ){
            return AjaxResult.error("地址信息不能为空");
        }else {
            List<AddressVO> list = appAddressMapper.getListById(addressVO.getId());
            if (list.isEmpty()){
                return AjaxResult.error("所选地址不存在");
            }
        }

        /**
         * 查询数据库有没有默认:如果有默认地址，设置为非默认
         */
        selectDefaultAddress(addressVO);

        addressVO.setUpdateBy(SecurityUtils.getUsername());
        addressVO.setUserId(SecurityUtils.getUserId());
        Integer nums = appAddressMapper.updateAddress(addressVO);
        if (1 != nums){
            return AjaxResult.error("更新失败");
        }
        return AjaxResult.success("更新成功");
    }

    /**
     * 设置默认地址
     * @param id
     * @return
     */
    @Override
    public AjaxResult setAddressDefault(Long id) {

        if (null == id){
            return AjaxResult.error("订单未选择");
        }
        AddressVO addressVO = new AddressVO();
        addressVO.setIsDefault(0);
        selectDefaultAddress(addressVO);
        Integer nums = appAddressMapper.setAddressDefault(id);
        if (nums != 1 ){
            throw new CustomException("设置默认地址失败");
        }
        return AjaxResult.success("设置默认地址成功");
    }

    /**
     * 创建地址或者更新地址时,查询数据库有没有默认:如果有默认地址，设置为非默认
     */
    private void selectDefaultAddress(AddressVO addressVO){
        if ( 0 == addressVO.getIsDefault() ){
            List<AddressVO> list = appAddressMapper.getAddressDefault(SecurityUtils.getUserId());
            if (list != null ){
                /**
                 * 更新为非默认
                 */
                Integer nums = updateNotDefault(list.get(0).getId());
                if (1 != nums){
                    throw new CustomException("更新非默认地址失败");
                }
            }
        }
    }

    /**
     * 把默认地址更新为非默认
     * @param id
     * @return
     */
    private Integer updateNotDefault(Long id) {
        return appAddressMapper.updateNotDefault(id);
    }
}
