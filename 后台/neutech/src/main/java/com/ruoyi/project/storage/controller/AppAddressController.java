package com.ruoyi.project.storage.controller;

import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.storage.domain.AddressVO;
import com.ruoyi.project.storage.service.AppAddressService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/app")
public class AppAddressController extends BaseController {

    @Resource
    private AppAddressService appAddressService;

    /**
     * 获取地址列表
     * @return
     */
    @GetMapping(value = "/address/list")
    public TableDataInfo getList(){
        startPage();

        List<AddressVO> list = appAddressService.getList();

        return getDataTable(list);
    }

    /**
     * 获取默认地址
     */
    @GetMapping(value = "/address/list/default")
    public AjaxResult getAddressDefault(){
        return appAddressService.getAddressDefault();
    }

    /**
     * 地址新增
     * @param addressVO
     * @return
     */
    @PostMapping(value = "/address/create")
    public AjaxResult createAddress(@RequestBody AddressVO addressVO){
        return appAddressService.createAddress(addressVO);
    }

    /**
     * 地址删除
     * @param id
     * @return
     */
    @DeleteMapping(value = "/address/delete/{id}")
    public AjaxResult deleteAddress(@PathVariable("id") Long id){
        return appAddressService.deleteAddress(id);
    }

    /**
     * 地址编辑
     * @param addressVO
     * @return
     */
    @PutMapping(value = "/address/update")
    public AjaxResult updateAddress(@RequestBody AddressVO addressVO){
        return appAddressService.updateAddress(addressVO);
    }

    /**
     * 设置默认地址
     * @param id
     * @return
     */
    @PutMapping(value = "/address/default/{id}")
    public AjaxResult setAddressDefault(@PathVariable(value = "id") Long id){
        return appAddressService.setAddressDefault(id);
    }

}
