package com.ruoyi.project.storage.controller;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.storage.domain.AdviceVO;
import com.ruoyi.project.storage.service.AppAdviceService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/app")
public class AppAdviceController {

    @Resource
    private AppAdviceService appAdviceService;

    @PostMapping(value = "/personal/advice")
    public AjaxResult setAdvice(@RequestBody AdviceVO adviceVO){
        return appAdviceService.setAdvice(adviceVO);
    }
}
