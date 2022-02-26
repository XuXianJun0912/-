package com.ruoyi.project.storage.domain;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class AdvertisementVO {

    private Long id;

    private String title;

    private String content;

    private String imgUrl;

    private Integer points;

    private Integer isEnable;

    private Integer sortNo;

    private Date createTime;

    private String createBy;

    private Date updateTime;

    private String updateBy;

    private Integer version;

    private Integer delFlag;

}
