package com.ruoyi.project.storage.service;

import com.ruoyi.framework.web.domain.AjaxResult;

import javax.servlet.http.HttpServletRequest;

public interface AppUserInfoService {
    AjaxResult updatePassword(HttpServletRequest request, String oldPassword, String newPassword);
}
