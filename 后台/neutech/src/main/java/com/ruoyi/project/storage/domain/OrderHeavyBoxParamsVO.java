package com.ruoyi.project.storage.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OrderHeavyBoxParamsVO {

    private String  orderName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date heavyBoxOrderTime;

    private String  heavyBoxCallName;

    private String  heavyBoxCallPhone;

    private String  heavyBoxCallAddress;

    private Date  heavyBoxCallTime;

    private String  heavyBoxCallInterval;


}
