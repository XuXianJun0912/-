package com.ruoyi.project.storage.mapper;

import com.ruoyi.project.storage.domain.AddressVO;
import com.ruoyi.project.storage.domain.OrderEmptyBoxParamsVO;

import java.util.List;

public interface AppAddressMapper {
    List<AddressVO> getList(Long userId);

    List<AddressVO> getAddressDefault(Long userId);

    Integer createAddress(AddressVO addressVO);

    Integer updateNotDefault(Long id);

    Integer deleteAddress(Long id);

    Integer updateAddress(AddressVO addressVO);

    List<AddressVO> getListById(Long id);

    Integer setAddressDefault(Long id);

    Integer orderBox(OrderEmptyBoxParamsVO orderEmptyBoxParamsVO);

}
