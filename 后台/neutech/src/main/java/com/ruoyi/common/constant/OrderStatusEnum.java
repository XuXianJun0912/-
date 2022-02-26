package com.ruoyi.common.constant;

import lombok.Getter;

@Getter
public enum OrderStatusEnum {

    BACKEND_SEND_EMPTY("backend",2,"发送空箱");

    private String terminal;
    private Integer status;
    private String buttonName;

    OrderStatusEnum(String terminal, Integer status, String buttonName) {
        this.terminal = terminal;
        this.status = status;
        this.buttonName = buttonName;
    }

    public static OrderStatusEnum orderStatusEnum(String terminal,Integer status){

        for(OrderStatusEnum orderStatusEnum : OrderStatusEnum.values()){
            if (terminal.equals(orderStatusEnum.getTerminal()) && status.intValue() == orderStatusEnum.getStatus()){
                return orderStatusEnum;
            }
        }
        return null;
    }
}
