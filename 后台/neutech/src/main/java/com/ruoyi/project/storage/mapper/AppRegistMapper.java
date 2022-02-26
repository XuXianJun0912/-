package com.ruoyi.project.storage.mapper;

import com.ruoyi.project.storage.domain.CustomerVO;

public interface AppRegistMapper {
    Integer registAppUser(CustomerVO customerVO);

    CustomerVO findCustomer(String userName);
}
