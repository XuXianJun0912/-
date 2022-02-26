package com.ruoyi.project.storage.controller;

import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.storage.domain.BoxInfoVO;
import com.ruoyi.project.storage.service.ManagerBoxInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/backend")
public class ManagerBoxInfoController extends BaseController {

    @Resource
    private ManagerBoxInfoService managerBoxInfoService;

    /**
     * 箱子信息列表（分页）
     * @param boxInfoVO
     * @return
     */
    @GetMapping(value = "/box/info/list")
    public TableDataInfo getList(BoxInfoVO boxInfoVO){

        startPage();
        List<BoxInfoVO> list = managerBoxInfoService.getList(boxInfoVO);

        return getDataTable(list);
    }

    /**
     * 箱子信息新增
     * @return
     */
    @PostMapping(value = "/box/info/create")
    public AjaxResult createBoxInfo(@RequestBody BoxInfoVO boxInfoVO){

        return managerBoxInfoService.createBoxInfo(boxInfoVO);
    }

    /**
     * 箱子信息删除
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/box/info/delete/{ids}")
    public AjaxResult deleteBoxInfo(@PathVariable(value = "ids") Long[] ids){

        return managerBoxInfoService.deleteBoxInfo(ids);
    }
}
