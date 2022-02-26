package com.ruoyi.project.storage.service;


import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.storage.domain.CustomerVO;

import java.util.List;

public interface ManagerCustomerService {
    List<CustomerVO> getList(String searchValue);

    AjaxResult saveCustomer(CustomerVO customerVO);

    AjaxResult updateCustomer(CustomerVO customerVO);

    AjaxResult deleteCustomer(Long[] userIds);

    AjaxResult enableStatus(String operate, Long[] userIds);

    AjaxResult resetPassword(Long[] userIds);
}
