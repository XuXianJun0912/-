package com.ruoyi.project.storage.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


public class BoxInfoVO {

    private Long id;
    private Long boxCode;
    private String boxStandard;
    private Long boxUnitPrice;
    private Long usedBy;
    private String usedByName;
    private Integer isUsed;
    private String remark;
    private Long sortNo;
    private Date createTime;
    private String createBy;
    private Date updateTime;
    private String updateBy;
    private Long version;
    private String delFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBoxCode() {
        return boxCode;
    }

    public void setBoxCode(Long boxCode) {
        this.boxCode = boxCode;
    }

    public String getBoxStandard() {
//        if (null != boxStandard){
//            return boxStandard.trim();
//        }
        return boxStandard;
    }

    public void setBoxStandard(String boxStandard) {
        this.boxStandard = boxStandard;
    }

    public Long getBoxUnitPrice() {
        return boxUnitPrice;
    }

    public void setBoxUnitPrice(Long boxUnitPrice) {
        this.boxUnitPrice = boxUnitPrice;
    }

    public Long getUsedBy() {
        return usedBy;
    }

    public void setUsedBy(Long usedBy) {
        this.usedBy = usedBy;
    }

    public String getUsedByName() {
        return usedByName;
    }

    public void setUsedByName(String usedByName) {
        this.usedByName = usedByName;
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getSortNo() {
        return sortNo;
    }

    public void setSortNo(Long sortNo) {
        this.sortNo = sortNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
}
