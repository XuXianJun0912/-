package com.ruoyi.project.storage.mapper;

import com.ruoyi.project.storage.domain.ConsumerVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ManagerConsumerMapper {
    List<ConsumerVO> getList(@Param(value = "consumerVO") ConsumerVO consumerVO);

    Integer createConsumer(ConsumerVO consumerVO);

    Integer updateConsumer(ConsumerVO consumerVO);

    Integer deleteConsumer(Map<String, Object> map);

    Integer enableConsumer(Map<String, Object> map);

    Integer resetPassword(Map<String, Object> map);
}
