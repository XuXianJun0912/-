package com.ruoyi.project.storage.service.impl;

import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.security.service.TokenService;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.storage.mapper.AppUserInfoMapper;
import com.ruoyi.project.storage.service.AppUserInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
@Transactional(rollbackFor = Exception.class)
public class AppUserInfoServiceImpl implements AppUserInfoService {

    @Resource
    private AppUserInfoMapper appUserInfoMapper;

    @Resource
    private TokenService tokenService;

    @Override
    public AjaxResult updatePassword(HttpServletRequest request, String oldPassword, String newPassword) {

        if ( StringUtils.isEmpty(oldPassword) ){
            throw new CustomException("旧密码不能为空");
        }

        if (StringUtils.isEmpty(newPassword)){
            throw new CustomException("新密码不能为空");
        }

        if (oldPassword.equals(newPassword)){
            throw new CustomException("输入的新旧密码不能一致");
        }

        /**
         * 获取用户的身份信息
         */
        LoginUser loginUser = tokenService.getLoginUser(request);
        String userName = loginUser.getUsername();
        /**
         * 从token中得到加密后的密码
         */
        String password = loginUser.getPassword();
        /**
         * 将上面得到的加密后的密码和输入的旧密码比较
         */
        boolean b = SecurityUtils.matchesPassword(oldPassword , password);
        if (b){
            /**
             * 新密码和旧密码不能相同
             */
            if(SecurityUtils.matchesPassword(newPassword , password)){
                throw new CustomException("输入的新密码和旧密码不能相同");
            }else {
                /**
                 * 新密码更新到数据库
                 */
                Integer nums = appUserInfoMapper.updatePassword(userName,SecurityUtils.encryptPassword(newPassword));
                if (1 != nums){
                    throw new CustomException("密码修改失败");
                }
                /**
                 * 新密码更新到缓存redis
                 */
                loginUser.getUser().setPassword(SecurityUtils.encryptPassword(newPassword));
                tokenService.setLoginUser(loginUser);
            }
        }
        return AjaxResult.success("密码修改成功");
    }
}
