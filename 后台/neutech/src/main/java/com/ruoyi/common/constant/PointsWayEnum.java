package com.ruoyi.common.constant;

import lombok.Getter;

@Getter
public enum PointsWayEnum {
    /**
     * 1：注册赠送；2：活动获得；3：积分使用
     */
    REGIST(1),
    AD(2),
    USED(3)
    ;
    private final Integer way;

    PointsWayEnum(Integer way) {
        this.way = way;
    }
}
