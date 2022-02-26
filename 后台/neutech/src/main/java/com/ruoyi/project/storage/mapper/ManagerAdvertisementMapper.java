package com.ruoyi.project.storage.mapper;

import com.ruoyi.project.storage.domain.AdvertisementVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ManagerAdvertisementMapper {

    List<AdvertisementVO> getList(@Param("searchValue")String searchValue);

    Integer saveAdvertisement(AdvertisementVO advertisementVO);

    Integer updateAdvertisement(AdvertisementVO advertisementVO);

    Integer updateIsAble(Map<String,Object> objectMap);

    Integer deleteAdvertisement( Map<String,Object> objectMap);
}
