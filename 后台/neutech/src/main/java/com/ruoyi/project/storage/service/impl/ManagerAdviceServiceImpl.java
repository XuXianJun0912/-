package com.ruoyi.project.storage.service.impl;

import com.ruoyi.project.storage.domain.AdviceVO;
import com.ruoyi.project.storage.mapper.ManagerAdviceMapper;
import com.ruoyi.project.storage.service.ManagerAdviceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ManagerAdviceServiceImpl implements ManagerAdviceService {

    @Resource
    private ManagerAdviceMapper managerAdviceMapper;

    @Override
    public List<AdviceVO> getList(String searchValue) {
        List<AdviceVO> list = managerAdviceMapper.getList(searchValue);
        return list;
    }
}
