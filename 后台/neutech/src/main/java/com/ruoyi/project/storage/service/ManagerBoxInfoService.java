package com.ruoyi.project.storage.service;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.storage.domain.BoxInfoVO;

import java.util.List;

public interface ManagerBoxInfoService {
    List<BoxInfoVO> getList(BoxInfoVO boxInfoVO);

    AjaxResult createBoxInfo(BoxInfoVO boxInfoVO);

    AjaxResult deleteBoxInfo(Long[] ids);
}
