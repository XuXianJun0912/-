package com.ruoyi.project.storage.service.impl;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.storage.domain.CustomerVO;
import com.ruoyi.project.storage.mapper.ManagerCustomerMapper;
import com.ruoyi.project.storage.service.ManagerCustomerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class ManagerCustomerServiceImpl implements ManagerCustomerService {



    @Value("${default.password.customer:abc123456}")
    private String dePassword;

    @Resource
    private ManagerCustomerMapper managerCustomerMapper;

//    客户列表（分页）
    @Override
    public List<CustomerVO> getList(String searchValue) {
        List<CustomerVO> list = managerCustomerMapper.getList(searchValue);
        return list;
    }

//    客户新增
    @Override
    public AjaxResult saveCustomer( CustomerVO customerVO) {

        AjaxResult ajaxResult = getCheckResult(customerVO);
        if ((Integer)(ajaxResult.get(UserConstants.CODE)) != 200){
            return ajaxResult;
        }

//        账号唯一性
        String userName = customerVO.getUserName();
        int countUserName = managerCustomerMapper.findCustomerByUserName(userName);
        if (countUserName > 0){
            return AjaxResult.error("用户已存在");
        }

//        手机号唯一性
        String phone = customerVO.getPhonenumber();
        int countPhone = managerCustomerMapper.findCustomerByPhone(phone);
        if (countPhone > 0){
            return AjaxResult.error("手机号已存在");
        }

//        邮箱唯一性
        String email = customerVO.getEmail();
        int countEmail = managerCustomerMapper.findCustomerByEm(email);
        if (countEmail > 0){
            return AjaxResult.error("邮箱已存在");
        }

//        默认值
        customerVO.setCreateBy(SecurityUtils.getUsername());
        customerVO.setUpdateBy(SecurityUtils.getUsername());
        customerVO.setVersion((long) 0);
//        设置默认密码
        customerVO.setPassword(SecurityUtils.encryptPassword(dePassword));
        customerVO.setStatus("0");
        customerVO.setDelFlag(String.valueOf(0));

        Integer nums = managerCustomerMapper.saveCustomer(customerVO);
        if (nums > 0){
            return AjaxResult.success("新增成功");
        }
        return AjaxResult.error("新增失败");
    }

//    客户编辑

    @Override
    public AjaxResult updateCustomer(CustomerVO customerVO) {
        AjaxResult ajaxResult = getCheckResult(customerVO);
        if ((Integer)(ajaxResult.get(UserConstants.CODE)) != 200){
            return ajaxResult;
        }

////        手机号校验
//        Integer countPhone = managerCustomerMapper.findCustomerByPhoneNum(customerVO);
//        if ( countPhone > 0 ){
//            return AjaxResult.error("手机号不能重复");
//        }
//
////        邮箱校验
//        Integer countEmail = managerCustomerMapper.findCustomerByEmail(customerVO);
//        if ( countEmail > 0 ){
//            return AjaxResult.error("邮箱不能重复");
//        }
//        默认值
        customerVO.setUpdateBy(SecurityUtils.getUsername());

        Integer nums = managerCustomerMapper.updateCustomer(customerVO);
        if (nums > 0){
            return AjaxResult.success("更新成功");
        }

        return AjaxResult.error("更新失败");
    }

//     校验
    private AjaxResult getCheckResult(CustomerVO customerVO){

        if (customerVO == null){
            return AjaxResult.error("参数不能为空");
        }
        if ( StringUtils.isEmpty(customerVO.getUserName()) ){
            return AjaxResult.error("账号不能为空");
        }
        if (customerVO.getUserName().length()>16) {
            return AjaxResult.error("最大长度为16");
        }
        if ( StringUtils.isEmpty(customerVO.getNickName()) ){
            return AjaxResult.error("姓名不能为空");
        }
        if ( customerVO.getNickName().length()>10 ){
            return AjaxResult.error("nickName长度最大为10");
        }
        if ( StringUtils.isEmpty(customerVO.getPhonenumber())){
            return AjaxResult.error("手机号不能为空");
        }
        if (customerVO.getPhonenumber().length()>11){
            return AjaxResult.error("手机号长度不能超过11");
        }
        boolean isMatch1 = Pattern.matches(UserConstants.PHONE_CHINA_PATTERN_REGEX, customerVO.getPhonenumber());
        if (!isMatch1){
            return AjaxResult.error("手机号格式不正确");
        }
        if ( StringUtils.isEmpty(customerVO.getEmail()) ){
            return AjaxResult.error("邮箱不能为空");
        }
        boolean isMatch2 = Pattern.matches(UserConstants.EMAIL_REGEX, customerVO.getEmail());
        if (!isMatch2){
            return AjaxResult.error("邮箱格式不正确");
        }
        return AjaxResult.success();
    }

//    客户删除

    @Override
    public AjaxResult deleteCustomer(Long[] userIds) {

        Map<String,Object> map = new HashMap<>();
        map.put("userIds" , Arrays.asList(userIds));
        map.put("updateBy" , SecurityUtils.getUsername());

        Integer nums = managerCustomerMapper.deleteCustomer(map);
        return AjaxResult.success(nums);
    }

//    启用停用
    @Override
    public AjaxResult enableStatus(String operate, Long[] userIds) {

        Map<String,Object> map = new HashMap<>();
        if ("enable".equals(operate)){
            //开启
            map.put("status" , "0");
        }else if ("disable".equals(operate)){
            //关闭
            map.put("status" , "1");
        }
        map.put("userIds" , Arrays.asList(userIds));
        map.put("updateBy" , SecurityUtils.getUsername());

        Integer nums = managerCustomerMapper.enableStatus(map);
        if (nums > 0){
            return AjaxResult.success(nums);
        }
        return AjaxResult.success("操作失败");
    }

//    客户密码重置

    @Override
    public AjaxResult resetPassword(Long[] userIds) {

        Map<String,Object> map = new HashMap<>();
        map.put("password",SecurityUtils.encryptPassword(dePassword));
        map.put("userId",Arrays.asList(userIds));
        map.put("updateBy",SecurityUtils.getUsername());
        Integer nums = managerCustomerMapper.resetPassword(map);
        if (nums <= 0){
            return AjaxResult.error("密码重置失败");
        }
        return AjaxResult.success("密码重置成功");
    }
}
