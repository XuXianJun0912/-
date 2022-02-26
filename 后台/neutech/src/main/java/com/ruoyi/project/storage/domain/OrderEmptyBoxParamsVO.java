package com.ruoyi.project.storage.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class OrderEmptyBoxParamsVO {

    private String  orderName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date emptyBoxOrderTime;

    private String  emptyBoxCallName;

    private String  emptyBoxCallPhone;

    private String  emptyBoxCallAddress;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date  emptyBoxCallTime;

    private String  emptyBoxCallInterval;

    private String  boxStandard;

    private Long  boxUnitPrice;

    private Integer  leaseDuration;

    private Long  totalPrice;

}
