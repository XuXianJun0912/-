package com.ruoyi.project.storage.controller;

import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.storage.domain.AdviceVO;
import com.ruoyi.project.storage.service.ManagerAdviceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/backend")
public class ManagerAdviceController extends BaseController {

    @Resource
    private ManagerAdviceService managerAdviceService;

    @GetMapping(value = "/advice/list")
    public TableDataInfo getList(String searchValue){

        startPage();
        List<AdviceVO> list = managerAdviceService.getList(searchValue);
        return getDataTable(list);
    }
}
