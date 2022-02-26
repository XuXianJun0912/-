package com.ruoyi.project.storage.mapper;

import com.ruoyi.project.storage.domain.BoxStandardVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ManagerBoxStandardMapper {
    List<BoxStandardVO> getList(@Param(value = "searchValue") String searchValue);

    Integer saveBoxStandard(BoxStandardVO boxStandardVO);

    Integer deleteBox( @Param(value = "ids") Long[] ids , @Param(value = "updateBy") String updateBy);

    BoxStandardVO findBoxStandard(String boxStandard);
}
