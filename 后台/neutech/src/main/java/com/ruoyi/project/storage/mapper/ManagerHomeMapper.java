package com.ruoyi.project.storage.mapper;

import org.apache.ibatis.annotations.Param;

public interface ManagerHomeMapper {

    Integer changePassword(@Param("userName") String userName , @Param("encryptPassword") String encryptPassword);
}
