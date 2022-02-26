package com.ruoyi.project.storage.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class AddressVO {

    private Long id;

    private Long userId;

    private String contacts;

    private String contactsPhone;

    private String province;

    private String provinceName;

    private String city;

    private String cityName;

    private String area;

    private String areaName;

    private String address;

    private Integer isDefault;

    private Long sortNo;

    private Date createTime;

    private String createBy;

    private Date updateTime;

    private String updateBy;

    private Integer version;

    private Integer delFlag;
}
