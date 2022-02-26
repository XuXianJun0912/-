package com.ruoyi.project.storage.service.impl;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.security.service.TokenService;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.storage.mapper.ManagerHomeMapper;
import com.ruoyi.project.storage.service.ManagerHomeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
public class ManagerHomeServiceImpl implements ManagerHomeService {

    @Resource
    private TokenService tokenService;

    @Resource
    private ManagerHomeMapper managerHomeMapper;

    @Override
    public AjaxResult changePassword(HttpServletRequest request, String oldPassword, String newPassword) {

        if (StringUtils.isEmpty(oldPassword)){
            return AjaxResult.error("旧密码不能为空");
        }
        if (StringUtils.isEmpty(newPassword)){
            return AjaxResult.error("新密码不能为空");
        }
        if (oldPassword.equals(newPassword)){
            return AjaxResult.error("输入的新旧密码不能相同");
        }

        LoginUser loginUser = tokenService.getLoginUser(request);

        String userName = loginUser.getUsername();
        String password = loginUser.getPassword();

        boolean b1 = SecurityUtils.matchesPassword(oldPassword,password);
        if (!b1){
            AjaxResult.error("旧密码输入错误");
        }

        boolean b2 = SecurityUtils.matchesPassword(newPassword,password);
        if (b2){
            AjaxResult.error("新密码和旧密码重复");
        }

        String ePassword  = SecurityUtils.encryptPassword(newPassword);
        //更新数据库密码
        Integer nums = managerHomeMapper.changePassword(userName,ePassword);

        if (nums == 0 ){
            return AjaxResult.error("修改失败");
        }
        //        将新密码放入当前登录用户的缓存
        loginUser.getUser().setPassword(ePassword);
        //        刷新token，重新赋值到缓存中
        tokenService.refreshToken(loginUser);

        return AjaxResult.success("修改成功");
    }
}
