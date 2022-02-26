package com.ruoyi.project.storage.controller;

import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.storage.domain.ConsumerVO;
import com.ruoyi.project.storage.service.ManagerConsumerService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/backend")
public class ManagerConsumerController extends BaseController {

    @Resource
    private ManagerConsumerService managerConsumerService;

//    用户列表（分页）
    @GetMapping(value = "/user/list")
    public TableDataInfo getList(ConsumerVO consumerVO){
        startPage();
        List<ConsumerVO>  list = managerConsumerService.getList(consumerVO);
        return getDataTable(list);
    }

//    用户新增
    @PostMapping(value = "/user/create")
    public AjaxResult createConsumer(@RequestBody ConsumerVO consumerVO){
        return managerConsumerService.createConsumer(consumerVO);
    }

//    用户编辑
    @PutMapping(value = "/user/update")
    public AjaxResult updateConsumer(@RequestBody ConsumerVO consumerVO){
        return managerConsumerService.updateConsumer(consumerVO);
    }

//    用户删除
    @DeleteMapping(value = "/user/delete/{userIds}")
    public AjaxResult deleteConsumer(@PathVariable(value = "userIds") Long[] userIds){
        return managerConsumerService.deleteConsumer(userIds);
    }

//    用户重置密码
    @PutMapping(value = "/user/reset/{userIds}")
    public AjaxResult resetPassword(@PathVariable(value = "userIds") Long[] userIds){
        return managerConsumerService.resetPassword(userIds);
    }

//    用户启用/停用
    @PutMapping(value = "/user/{operate}/{userIds}")
    public AjaxResult enableConsumer(@PathVariable(value = "operate") String operate,@PathVariable(value = "userIds") Long[] userIds){
        return managerConsumerService.enableConsumer(operate,userIds);
    }

}
