package com.ruoyi.project.storage.service;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.storage.domain.BoxStandardVO;

import java.util.List;

public interface ManagerBoxStandardService {
    List<BoxStandardVO> getList(String searchValue);

    AjaxResult saveBoxStandard(BoxStandardVO boxStandardVO);

    AjaxResult deleteBox(Long[] ids);

    AjaxResult getBoxStandardSelect();
}
