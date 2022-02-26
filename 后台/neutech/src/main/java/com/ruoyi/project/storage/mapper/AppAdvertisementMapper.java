package com.ruoyi.project.storage.mapper;

import com.ruoyi.project.storage.domain.AdvertisementVO;
import com.ruoyi.project.storage.domain.ParamsAdvertisementVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppAdvertisementMapper {
    List<AdvertisementVO> getList();

    Integer setPoints(ParamsAdvertisementVO paramsAdvertisementVO);

    AdvertisementVO getAdverInfo(@Param("id") Long id);
}
