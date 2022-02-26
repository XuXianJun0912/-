package com.ruoyi.project.storage.service;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.storage.domain.AddressVO;

import java.util.List;

public interface AppAddressService {
    List<AddressVO> getList();

    AjaxResult getAddressDefault();

    AjaxResult createAddress(AddressVO addressVO);

    AjaxResult deleteAddress(Long id);

    AjaxResult updateAddress(AddressVO addressVO);

    AjaxResult setAddressDefault(Long id);
}
