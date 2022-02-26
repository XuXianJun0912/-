package com.ruoyi.project.storage.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PointRecordVO {

    private Long id;

    private Long userId;

    private Integer way;

    private Long points;

    private Long advertisementId;

    private Long orderId;

    private Long sortNo;

    private Date createTime;

    private String createBy;

    private Date updateTime;

    private String updateBy;

    private Long version;

    private Integer delFlag;


}
