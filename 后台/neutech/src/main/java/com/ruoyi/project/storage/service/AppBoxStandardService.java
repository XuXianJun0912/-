package com.ruoyi.project.storage.service;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.storage.domain.OrderEmptyBoxParamsVO;

public interface AppBoxStandardService {

    AjaxResult getBoxDefaultAddress();

    AjaxResult selectBoxStandard();

    AjaxResult orderBox(OrderEmptyBoxParamsVO orderEmptyBoxParamsVO);
}
