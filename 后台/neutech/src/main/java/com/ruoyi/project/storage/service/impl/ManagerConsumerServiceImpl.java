package com.ruoyi.project.storage.service.impl;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.storage.domain.ConsumerVO;
import com.ruoyi.project.storage.domain.CustomerVO;
import com.ruoyi.project.storage.mapper.ManagerConsumerMapper;
import com.ruoyi.project.storage.mapper.ManagerCustomerMapper;
import com.ruoyi.project.storage.service.ManagerConsumerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class ManagerConsumerServiceImpl implements ManagerConsumerService {

    @Value("${default.password.consumer:123456}")
    private String dePassword;

    @Resource
    private ManagerConsumerMapper managerConsumerMapper;

    @Resource
    private ManagerCustomerMapper managerCustomerMapper;

    //     校验
    private AjaxResult getCheckResult(ConsumerVO consumerVO){

        if (consumerVO == null){
            return AjaxResult.error("参数不能为空");
        }
        if ( StringUtils.isEmpty(consumerVO.getUserName()) ){
            return AjaxResult.error("账号不能为空");
        }
        if (consumerVO.getUserName().length()>16) {
            return AjaxResult.error("最大长度为16");
        }
        if ( StringUtils.isEmpty(consumerVO.getNickName()) ){
            return AjaxResult.error("姓名不能为空");
        }
        if ( consumerVO.getNickName().length()>10 ){
            return AjaxResult.error("nickName长度最大为10");
        }
        if ( StringUtils.isEmpty(consumerVO.getPhonenumber())){
            return AjaxResult.error("手机号不能为空");
        }
        if (consumerVO.getPhonenumber().length()>11){
            return AjaxResult.error("手机号长度不能超过11");
        }
        boolean isMatch1 = Pattern.matches(UserConstants.PHONE_CHINA_PATTERN_REGEX, consumerVO.getPhonenumber());
        if (!isMatch1){
            return AjaxResult.error("手机号格式不正确");
        }
        if ( StringUtils.isEmpty(consumerVO.getEmail()) ){
            return AjaxResult.error("邮箱不能为空");
        }
        boolean isMatch2 = Pattern.matches(UserConstants.EMAIL_REGEX, consumerVO.getEmail());
        if (!isMatch2){
            return AjaxResult.error("邮箱格式不正确");
        }
        return AjaxResult.success();
    }

    /**
     * 用户查询
     * @param consumerVO
     * @return
     */
    @Override
    public List<ConsumerVO> getList(ConsumerVO consumerVO) {
        List<ConsumerVO>  list =  managerConsumerMapper.getList(consumerVO);
        return list;
    }

    /**
     * 用户新增
     * @param consumerVO
     * @return
     */
    @Override
    public AjaxResult createConsumer(ConsumerVO consumerVO) {
        AjaxResult ajaxResult = getCheckResult(consumerVO);
        if ((Integer) ajaxResult.get(UserConstants.CODE) != 200){
            return ajaxResult;
        }
//        账号唯一性
        String userName = consumerVO.getUserName();
        int countUserName = managerCustomerMapper.findCustomerByUserName(userName);
        if (countUserName > 0){
            return AjaxResult.error("用户已存在");
        }

//        手机号唯一性
        String phone = consumerVO.getPhonenumber();
        int countPhone = managerCustomerMapper.findCustomerByPhone(phone);
        if (countPhone > 0){
            return AjaxResult.error("手机号已存在");
        }

//        邮箱唯一性
        String email = consumerVO.getEmail();
        int countEmail = managerCustomerMapper.findCustomerByEm(email);
        if (countEmail > 0){
            return AjaxResult.error("邮箱已存在");
        }

//        默认值
        consumerVO.setCreateBy(SecurityUtils.getUsername());
        consumerVO.setUpdateBy(SecurityUtils.getUsername());
        consumerVO.setVersion((long) 0);
//        设置默认密码
        consumerVO.setPassword(SecurityUtils.encryptPassword(dePassword));
        consumerVO.setStatus("0");
        consumerVO.setDelFlag(String.valueOf(0));

        Integer nums = managerConsumerMapper.createConsumer(consumerVO);
        if (nums > 0){
            return AjaxResult.success("新增成功");
        }
        return AjaxResult.error("新增失败");
    }

    /**
     * 用户编辑
     * @param consumerVO
     * @return
     */
    @Override
    public AjaxResult updateConsumer(ConsumerVO consumerVO) {

        AjaxResult ajaxResult = getCheckResult(consumerVO);
        if ((Integer) ajaxResult.get(UserConstants.CODE) != 200){
            return ajaxResult;
        }
////        账号唯一性
//        String userName = consumerVO.getUserName();
//        int countUserName = managerCustomerMapper.findCustomerByUserName(userName);
//        if (countUserName > 0){
//            return AjaxResult.error("用户已存在");
//        }

////        手机号唯一性
//        String phone = consumerVO.getPhonenumber();
//        int countPhone = managerCustomerMapper.findCustomerByPhone(phone);
//        if (countPhone > 0){
//            return AjaxResult.error("手机号已存在");
//        }

//        默认值
        consumerVO.setUpdateBy(SecurityUtils.getUsername());

        Integer nums = managerConsumerMapper.updateConsumer(consumerVO);
        if (nums > 0){
            return AjaxResult.success("更新成功");
        }

        return AjaxResult.error("更新失败");
    }

    /**
     * 用户删除
     * @param userIds
     * @return
     */
    @Override
    public AjaxResult deleteConsumer(Long[] userIds) {
        Map<String,Object> map = new HashMap<>();
        map.put("userIds" , Arrays.asList(userIds));
        map.put("updateBy" , SecurityUtils.getUsername());

        Integer nums = managerConsumerMapper.deleteConsumer(map);
        return AjaxResult.success(nums);
    }

    /**
     * 重置密码
     * @param userIds
     * @return
     */
    @Override
    public AjaxResult resetPassword(Long[] userIds) {
        Map<String,Object> map = new HashMap<>();
        map.put("password",SecurityUtils.encryptPassword(dePassword));
        map.put("userId",Arrays.asList(userIds));
        map.put("updateBy",SecurityUtils.getUsername());
        Integer nums = managerConsumerMapper.resetPassword(map);
        if (nums <= 0){
            return AjaxResult.error("密码重置失败");
        }
        return AjaxResult.success("密码重置成功");    }

    /**
     * 是否启用
     * @param operate
     * @param userIds
     * @return
     */
    @Override
    public AjaxResult enableConsumer(String operate, Long[] userIds) {

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

        Integer nums = managerConsumerMapper.enableConsumer(map);
        if (nums > 0){
            return AjaxResult.success(nums);
        }
        return AjaxResult.success("操作失败");    }
}
