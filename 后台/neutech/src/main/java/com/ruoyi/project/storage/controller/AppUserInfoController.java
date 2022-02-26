package com.ruoyi.project.storage.controller;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.storage.service.AppUserInfoService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/app")
public class AppUserInfoController {

    @Resource
    private AppUserInfoService appUserInfoService;

    @PutMapping(value = "/personal/updatePassword/{oldPassword}/{newPassword}")
    public AjaxResult updatePassword(@PathVariable(value = "oldPassword") String oldPassword ,
                                     @PathVariable(value = "newPassword") String newPassword,
                                     HttpServletRequest request){
        return appUserInfoService.updatePassword(request,oldPassword,newPassword);
    }
}
