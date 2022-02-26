package com.ruoyi.project.storage.controller;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.storage.domain.ParamsAdvertisementVO;
import com.ruoyi.project.storage.service.AppAdvertisementService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/app")
public class AppAdvertisementController {

    @Resource
    private AppAdvertisementService appAdvertisementService;

    /**
     * 广告列表（非分页）
     * @return
     */
    @GetMapping(value = "/advertisement/list")
    public AjaxResult getList(){
        return appAdvertisementService.getList();
    }

    /**
     * 广告详情
     */
    @GetMapping(value = "/advertisement/info/{id}")
    public AjaxResult getAdverInfo(@PathVariable(value = "id") Long id){
        return appAdvertisementService.getAdverInfo(id);
    }


    /**
     * 广告积分获取
     */
    @PutMapping(value = "/advertisement/points")
    public AjaxResult setPoints(@RequestBody ParamsAdvertisementVO paramsAdvertisementVO){
        return appAdvertisementService.setPoints(paramsAdvertisementVO);
    }
}
