package com.ruoyi.project.storage.controller;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.storage.domain.CustomerVO;
import com.ruoyi.project.storage.service.AppRegistService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/app")
public class AppRegistController {

    @Resource
    private AppRegistService appRegistServicel;

    @PostMapping(value = "/regist")
    public AjaxResult registUser(@RequestBody CustomerVO customerVO){

        return appRegistServicel.registUser(customerVO);
    }
}
