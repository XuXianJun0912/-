package com.ruoyi.project.storage.controller;

import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.storage.domain.BackendOrderVO;
import com.ruoyi.project.storage.domain.GetOrderListParamsVO;
import com.ruoyi.project.storage.service.ManagerOrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/backend")
public class ManagerOrderController extends BaseController {

    @Resource
    private ManagerOrderService managerOrderService;

    /**
     *订单列表（分页）
     * @param getOrderListParamsVO
     * @return
     */
    @GetMapping(value = "/order/list")
    public TableDataInfo getOrderList(GetOrderListParamsVO getOrderListParamsVO){

        startPage();

        List<BackendOrderVO> list = managerOrderService.getOrderList(getOrderListParamsVO);

        return getDataTable(list);
    }

    /**
     * 订单详情
     * @param id
     * @return
     */
    @GetMapping(value = "/order/info/{id}")
    public AjaxResult getOrderById(@PathVariable(value = "id") Long id){
        return managerOrderService.getOrderById(id);
    }

    /**
     * 订单删除
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/order/delete/{ids}")
    public AjaxResult deleteOrder(@PathVariable(value = "ids") Long[] ids){
        return managerOrderService.deleteOrder(ids);
    }

    /**
     * 订单操作
     * @param id
     * @param operate
     * @param version
     * @param backendOrderVO
     * @return
     */
    @PutMapping(value = "/order/operate/{id}/{operate}/{version}")
    public AjaxResult operateOrder(@PathVariable(value = "id") Long id,
                                   @PathVariable(value = "operate") Integer operate,
                                   @PathVariable(value = "version") Long version,
                                   @RequestBody BackendOrderVO backendOrderVO){
        return managerOrderService.operateOrder(id,operate,version,backendOrderVO);
    }

}
