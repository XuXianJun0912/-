package com.ruoyi.project.storage.service;

import com.ruoyi.project.storage.domain.AdviceVO;

import java.util.List;

public interface ManagerAdviceService {
    List<AdviceVO> getList(String searchValue);
}
