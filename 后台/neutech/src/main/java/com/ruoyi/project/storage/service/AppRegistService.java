package com.ruoyi.project.storage.service;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.storage.domain.CustomerVO;

public interface AppRegistService {
    AjaxResult registUser(CustomerVO customerVO);

}
