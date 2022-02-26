package com.ruoyi.project.storage.controller;

import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.storage.domain.CustomerVO;
import com.ruoyi.project.storage.service.ManagerCustomerService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/backend")
public class ManagerCustomerController extends BaseController {

    @Resource
    private ManagerCustomerService managerCustomerService;

//    客户列表（分页）
    @GetMapping(value = "/customer/list")
    public TableDataInfo getList(@RequestParam(required = false) String searchValue){

        startPage();
        List<CustomerVO> list = managerCustomerService.getList(searchValue);
        return getDataTable(list);

    }

//    客户新增
    @PostMapping(value ="/customer/create")
    public AjaxResult saveCustomer(@RequestBody CustomerVO customerVO){
        return managerCustomerService.saveCustomer(customerVO);
    }

//    客户编辑
    @PutMapping(value = "/customer/update")
    public AjaxResult updateCustomer(@RequestBody CustomerVO customerVO){
        return managerCustomerService.updateCustomer(customerVO);
    }

//    客户删除
    @DeleteMapping(value = "/customer/delete/{userIds}")
    public AjaxResult deleteCustomer(@PathVariable(value = "userIds") Long[] userIds){
        return managerCustomerService.deleteCustomer(userIds);
    }

//    启用/停用
    @PutMapping(value = "/customer/{operate}/{userIds}")
    public AjaxResult enableStatus(@PathVariable(value = "operate") String operate,
                                   @PathVariable(value = "userIds") Long[] userIds){
        return managerCustomerService.enableStatus(operate,userIds);
    }

//    客户重置密码
    @PutMapping(value = "/customer/reset/{userIds}")
    public AjaxResult resetPassword( @PathVariable(value = "userIds") Long[] userIds){
        return managerCustomerService.resetPassword(userIds);
    }

}
