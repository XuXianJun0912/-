package com.ruoyi.project.storage.mapper;

import com.ruoyi.project.storage.domain.BoxInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ManagerBoxInfoMapper {
    List<BoxInfoVO> getList( BoxInfoVO boxInfoVO);

    Integer createBoxInfo(BoxInfoVO boxInfoVO);

    Integer deleteBoxInfo(@Param(value = "ids") Long[] ids,@Param(value = "updateBy") String updateBy);

    Integer findBoxInfo(@Param(value = "ids") Long[] ids);

    Integer freeOrderBox(@Param("boxId") Long boxId);
}
