package com.ruoyi.project.storage.mapper;

import com.ruoyi.project.storage.domain.AdviceVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ManagerAdviceMapper {
    List<AdviceVO> getList(@Param("searchValue") String searchValue);
}
