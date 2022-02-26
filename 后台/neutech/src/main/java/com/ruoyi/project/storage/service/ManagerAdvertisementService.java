package com.ruoyi.project.storage.service;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.storage.domain.AdvertisementVO;

import java.util.List;

public interface ManagerAdvertisementService {


    List<AdvertisementVO> getList(String searchValue);

    Integer saveAdvertisement(AdvertisementVO advertisementVO);

    AjaxResult updateAdvertisement(AdvertisementVO advertisementVO);

    AjaxResult updateIsAble(String operate,Long[] ids);

    AjaxResult deleteAdvertisement(Long[] ids);
}
