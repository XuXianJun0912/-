package com.ruoyi.project.storage.controller;

import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.storage.domain.BoxStandardVO;
import com.ruoyi.project.storage.service.ManagerBoxStandardService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/backend")
public class ManagerBoxStandardController extends BaseController {

    @Resource
    private ManagerBoxStandardService managerBoxStandardService;

    /**
     * 箱子规格列表（分页）
     * @param searchValue
     * @return
     */
    @GetMapping(value = "/box/standard/list")
    public TableDataInfo getList(@RequestParam(required = false,value = "searchValue") String searchValue){

        startPage();
        List<BoxStandardVO> list = managerBoxStandardService.getList(searchValue);
        return getDataTable(list);
    }

    /**
     *箱子规格新增
     * @param boxStandardVO
     * @return
     */
    @PostMapping(value = "/box/standard/create")
    public AjaxResult saveBoxStandard(@RequestBody BoxStandardVO boxStandardVO){
        return managerBoxStandardService.saveBoxStandard(boxStandardVO);
    }

    /**
     * 箱子规格删除
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/box/standard/delete/{ids}")
    public AjaxResult deleteBox(@PathVariable(value = "ids") Long[] ids){
        return managerBoxStandardService.deleteBox(ids);
    }

    /**
     * 箱子规格下拉列表（非分页）
     * @return
     */
    @GetMapping(value = "/box/standard/select")
    public AjaxResult getBoxStandardSelect(){
        return managerBoxStandardService.getBoxStandardSelect();
    }
}
