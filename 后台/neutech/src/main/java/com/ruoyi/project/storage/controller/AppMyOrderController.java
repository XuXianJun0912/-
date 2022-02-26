package com.ruoyi.project.storage.controller;

import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.storage.domain.BackendOrderVO;
import com.ruoyi.project.storage.service.AppMyOrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/app")
public class AppMyOrderController extends BaseController {

    @Resource
    private AppMyOrderService appMyOrderService;

    @GetMapping(value = "/order/list")
    public TableDataInfo getList(){
        startPage();
        List<BackendOrderVO> list = appMyOrderService.getList();
        return getDataTable(list);
    }

    @GetMapping(value = "/order/info/{id}")
    public AjaxResult getOrderInfo(@PathVariable(value = "id") Long id){
        return appMyOrderService.getOrderInfo(id);
    }

    @PutMapping(value = "/order/operate/{id}/{operate}/{version}")
    public AjaxResult operateOrder(@PathVariable(value = "id") Long id ,
                                   @PathVariable(value = "operate") Integer operate ,
                                   @PathVariable(value = "version") Long	version ,
                                   @RequestBody BackendOrderVO backendOrderVO ){
        return appMyOrderService.operateOrder(id,operate,version,backendOrderVO);
    }


}
