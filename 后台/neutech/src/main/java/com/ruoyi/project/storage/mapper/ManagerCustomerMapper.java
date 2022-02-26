package com.ruoyi.project.storage.mapper;

import com.ruoyi.project.storage.domain.CustomerVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

public interface ManagerCustomerMapper {
    List<CustomerVO> getList(@Param("searchValue") String searchValue);

    Integer saveCustomer( CustomerVO customerVO);

    Integer findCustomerByUserName(String userName);

    Integer findCustomerByPhone(String phonenumber);

    Integer updateCustomer(CustomerVO customerVO);

    Integer findCustomerByPhoneNum(CustomerVO customerVO);

    Integer findCustomerByEmail(CustomerVO customerVO);

    Integer deleteCustomer(Map<String,Object> objectMap);

    Integer enableStatus(Map<String, Object> map);

    Integer resetPassword(Map<String, Object> map);

    Integer findCustomerByEm(String email);
}
