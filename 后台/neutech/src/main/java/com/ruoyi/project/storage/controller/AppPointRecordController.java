package com.ruoyi.project.storage.controller;

import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.storage.domain.PointRecordVO;
import com.ruoyi.project.storage.service.AppPointRecordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/app")
public class AppPointRecordController extends BaseController {

    @Resource
    private AppPointRecordService appPointRecordService;

    @GetMapping("/point/list")
    public TableDataInfo getList(){
        startPage();
        List<PointRecordVO> list = appPointRecordService.getList();
        return getDataTable(list);
    }

    @GetMapping("/point/select")
    public AjaxResult selectPoint(){
        return appPointRecordService.selectPoint();
    }

}
