package com.ruoyi.project.storage.controller;

import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.storage.domain.AdvertisementVO;
import com.ruoyi.project.storage.service.ManagerAdvertisementService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping(value = "/backend")
public class ManagerAdvertisementController extends BaseController {

    @Resource
    private ManagerAdvertisementService managerAdvertisementService;

//    广告列表（分页）
    @GetMapping(value = "/advertisement/list")
    public TableDataInfo getList(@RequestParam(required = false) String searchValue){

         startPage();
         List<AdvertisementVO> list = managerAdvertisementService.getList(searchValue);

         return  getDataTable(list);
    }


//    广告新增
    @PostMapping(value = "/advertisement/create")
    public AjaxResult saveAdvertisement(@RequestBody AdvertisementVO advertisementVO){

        Integer nums = managerAdvertisementService.saveAdvertisement(advertisementVO);

        if( nums != 1){
            return AjaxResult.error("保存失败");
        }

        return AjaxResult.success("新增成功");
    }


//    广告编辑
    @PutMapping(value = "/advertisement/update")
    public AjaxResult updateAdvertisement(@RequestBody AdvertisementVO advertisementVO){

        return managerAdvertisementService.updateAdvertisement(advertisementVO);

    }

//    广告启用/停用
    @PutMapping(value = "/advertisement/{operate}/{ids}")
    public AjaxResult updateIsAble(@PathVariable(value = "operate") String operate,@PathVariable(value = "ids") Long[] ids){

        return managerAdvertisementService.updateIsAble(operate,ids);
    }

//    广告删除
    @DeleteMapping(value = "/advertisement/delete/{ids}")
    public AjaxResult deleteAdvertisement(@PathVariable("ids") Long[] ids){
        return managerAdvertisementService.deleteAdvertisement(ids);
    }
}
