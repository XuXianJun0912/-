package com.ruoyi.project.storage.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BoxStandardVO {
    private Long id;
    private String boxStandard;
    private Long boxUnitPrice;
    private String remark;
    private Long sortNo;
    private Date createTime;
    private String createBy;
    private Date updateTime;
    private String updateBy;
    private Long version;
    private String delFlag;
    private Long totalNumber;
    private Long usedNumber;
    private Long inventoryNumber;
    private String useRatio;
}
