package com.ruoyi.project.storage.controller;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.storage.service.ManagerHomeService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/backend")
public class ManagerHomeController {

    @Resource
    private ManagerHomeService managerHomeService;

    @PutMapping(value = "/home/updatePassword/{oldPassword}/{newPassword}")
    public AjaxResult changePassword(HttpServletRequest request,
                                     @PathVariable(value = "oldPassword") String oldPassword,
                                     @PathVariable(value = "newPassword") String newPassword){
        return managerHomeService.changePassword(request,oldPassword,newPassword);
    }
}
