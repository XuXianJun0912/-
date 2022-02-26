package com.ruoyi.project.storage.service.impl;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.storage.domain.AdvertisementVO;
import com.ruoyi.project.storage.mapper.ManagerAdvertisementMapper;
import com.ruoyi.project.storage.service.ManagerAdvertisementService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ManagerAdvertisementServiceImpl implements ManagerAdvertisementService {

    @Resource
    private ManagerAdvertisementMapper managerAdvertisementMapper;

    @Override
    public List<AdvertisementVO> getList(String searchValue) {


        List<AdvertisementVO> list = managerAdvertisementMapper.getList(searchValue);
        return list;
    }

    @Override
    public Integer saveAdvertisement(AdvertisementVO advertisementVO) {
        advertisementVO.setCreateBy(SecurityUtils.getUsername());
        advertisementVO.setCreateTime(new Date());
        advertisementVO.setVersion(1);
        advertisementVO.setIsEnable(0);

        Integer nums = managerAdvertisementMapper.saveAdvertisement(advertisementVO);
        return nums;
    }


    @Override
    public AjaxResult updateAdvertisement(AdvertisementVO advertisementVO) {

        if (null != advertisementVO ){
            if (StringUtils.isEmpty(advertisementVO.getTitle())){
                return AjaxResult.error("title 不能为空");
            }
            if (StringUtils.isEmpty(advertisementVO.getContent())){
                return AjaxResult.error("content 不能为空");
            }
            if (advertisementVO.getTitle().length() > 20){
                return AjaxResult.error("title 标题超过限制20");
            }
            if (advertisementVO.getContent().length() > 1000){
                return AjaxResult.error("content 内容超过限制1000");
            }
            if (advertisementVO.getPoints().intValue() < 0){
                return AjaxResult.error("积分不能为负数。");
            }
        }

//        更新人信息
        advertisementVO.setUpdateBy(SecurityUtils.getUsername());
        advertisementVO.setIsEnable(0);
        Integer nums = managerAdvertisementMapper.updateAdvertisement(advertisementVO);

        if ( nums != 1 ){
            return AjaxResult.error("数据已经被修改，请刷新数据列表");
        }
        return AjaxResult.success("修改成功");
    }

    @Override
    public AjaxResult updateIsAble(String operate,Long[] ids) {

        Map<String,Object> map = new HashMap<>();
        if ("enable".equals(operate)){
            //开启
            map.put("isEnable" , "0");
        }else if ("disable".equals(operate)){
            //关闭
            map.put("isEnable" , "1");
        }
//        map.put("isEnable" , operate);
        map.put("ids" , Arrays.asList(ids));
        map.put("updateBy" , SecurityUtils.getUsername());

        Integer nums = managerAdvertisementMapper.updateIsAble(map);
        return AjaxResult.success(nums);
    }

    @Override
    public AjaxResult deleteAdvertisement(Long[] ids) {

        Map<String,Object> map = new HashMap<>();
        map.put("ids" , Arrays.asList(ids));
        map.put("updateBy" , SecurityUtils.getUsername());

        Integer nums = managerAdvertisementMapper.deleteAdvertisement(map);
        return AjaxResult.success(nums);
    }
}
