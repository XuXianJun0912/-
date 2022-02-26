package com.ruoyi.project.storage.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BackendOrderVO {
    private Long  id;
    private Long  userId;
    private String  nickName;
    private Integer  status;
    private Long  orderCode;
    private String  orderName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date emptyBoxOrderTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date  heavyBoxOrderTime;
    private String  emptyBoxCallName;
    private String  emptyBoxCallPhone;
    private String  emptyBoxCallAddress;
    private Date  emptyBoxCallTime;
    private String  emptyBoxCallInterval;
    private String  heavyBoxCallName;
    private String  heavyBoxCallPhone;
    private String  heavyBoxCallAddress;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date  heavyBoxCallTime;
    private String  heavyBoxCallInterval;
    private Long  boxId;
    private Long  boxCode;
    private String  boxStandard;
    private Long  boxUnitPrice;
    private Integer  leaseDuration;
    private Long  totalPrice;
    private Long  sortNo;
    private Date  createTime;
    private String  createBy;
    private Date  updateTime;
    private String  updateBy;
    private Long  version;
    private String  delFlag;
    private Integer  appDelFlag;
    private Integer  backendDelFlag;


}
