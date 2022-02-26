package com.ruoyi.project.storage.service;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.storage.domain.ParamsAdvertisementVO;

public interface AppAdvertisementService {
    AjaxResult getList();

    AjaxResult setPoints(ParamsAdvertisementVO paramsAdvertisementVO);

    AjaxResult getAdverInfo(Long id);
}
