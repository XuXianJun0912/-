package com.ruoyi.project.storage.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class CustomerVO {

    private Long userId ;
    private Long deptId ;
    private String userName ;
    private String nickName ;
    private String userType ;
    private String email ;
    private String phonenumber ;
    private String sex ;
    private String avatar ;
    private String password ;
    private String status ;
    private String delFlag ;
    private String loginIp ;
    private Date loginDate ;
    private String createBy ;
    private Date createTime ;
    private String updateBy ;
    private Date updateTime ;
    private String remark ;
    private Long currentPoints ;
    private Long version ;

}
