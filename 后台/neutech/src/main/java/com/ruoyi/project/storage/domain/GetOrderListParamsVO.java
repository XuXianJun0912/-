package com.ruoyi.project.storage.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetOrderListParamsVO {
    private String nickName ;
    private Integer	status  ;
    private String emptyBoxOrderTimeStart ;
    private String emptyBoxOrderTimeEnd ;
    private String heavyBoxOrderTimeStart ;
    private String heavyBoxOrderTimeEnd ;
}
