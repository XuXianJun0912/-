package com.ruoyi.project.storage.controller;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.storage.domain.OrderEmptyBoxParamsVO;
import com.ruoyi.project.storage.service.AppBoxStandardService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/app")
public class AppBoxStandardController {

    @Resource
    private AppBoxStandardService appBoxStandardService;

    /**
     * 获取当前用户默认收货地址
     * @return
     */
    @GetMapping("/box/defaultAddress")
    public AjaxResult getBoxDefaultAddress(){
        return appBoxStandardService.getBoxDefaultAddress();
    }

    /**
     * 有效箱子规格选择（非分页）
     * @return
     */
    @GetMapping("/box/standard/select")
    public AjaxResult selectBoxStandard(){
        return appBoxStandardService.selectBoxStandard();
    }

    /**
     * 预约
     * @param orderEmptyBoxParamsVO
     * @return
     */
    @PostMapping("/box/order")
    public AjaxResult orderBox(@RequestBody OrderEmptyBoxParamsVO orderEmptyBoxParamsVO){
        return appBoxStandardService.orderBox(orderEmptyBoxParamsVO);
    }

}
