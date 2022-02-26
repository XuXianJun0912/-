package com.ruoyi.project.storage.mapper;

import org.apache.ibatis.annotations.Param;

public interface AppUserInfoMapper {
    Integer updatePassword(@Param("userName") String userName, @Param("newPassword") String newPassword);
}
