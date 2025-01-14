package com.px.project.base.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.px.common.annotation.Excel;
import com.px.common.core.domain.BaseEntity;

/**
 * 区域对象 t_base_region
 *
 * @author 品讯科技
 * @date 2024-08
 */
public class TBaseRegion extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识id
     */
    private Long regionId;

    @TableField(exist = false)
    private Long[] regionIds;

    /**
     * 装置id
     */
    private Long plantId;

    @Excel(name = "装置名称")
    @TableField(exist = false)
    private String plantName;

    @Excel(name = "装置编码")
    @TableField(exist = false)
    private String plantCode;

    @Excel(name = "装置类别",dictType = "plant_type")
    @TableField(exist = false)
    private String plantType;

    /**
     * 区域编码
     */
    @Excel(name = "区域编号")
    private String regionCode;

    /**
     * 区域名称
     */
    @Excel(name = "区域名称")
    private String regionName;

    /**
     * 描述
     */
    @Excel(name = "描述")
    private String remarks;

    /**
     * 审核状态
     */
    @Excel(name = "审核状态",dictType = "base_approve_status")
    private Long approveStatus;

    /**
     * 最新申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date approveTime;

    /**
     * 部门编号
     */
    private Long deptId;

    /**
     * 状态 1 ：正常 ；0：删除
     */
    private Integer delFlag;

    /**
     * 创建人
     */
    private Long createrCode;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdate;

    /**
     * 修改人
     */
    @Excel(name = "修改人")
    @TableField(exist = false)
    private String updater;
    private Long updaterCode;

    public Long[] getRegionIds() {
        return regionIds;
    }

    public void setRegionIds(Long[] regionIds) {
        this.regionIds = regionIds;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getPlantCode() {
        return plantCode;
    }

    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }

    public String getPlantType() {
        return plantType;
    }

    public void setPlantType(String plantType) {
        this.plantType = plantType;
    }

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedate;

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setPlantId(Long plantId) {
        this.plantId = plantId;
    }

    public Long getPlantId() {
        return plantId;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setApproveStatus(Long approveStatus) {
        this.approveStatus = approveStatus;
    }

    public Long getApproveStatus() {
        return approveStatus;
    }

    public void setApproveTime(Date approveTime) {
        this.approveTime = approveTime;
    }

    public Date getApproveTime() {
        return approveTime;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setCreaterCode(Long createrCode) {
        this.createrCode = createrCode;
    }

    public Long getCreaterCode() {
        return createrCode;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setUpdaterCode(Long updaterCode) {
        this.updaterCode = updaterCode;
    }

    public Long getUpdaterCode() {
        return updaterCode;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("regionId", getRegionId())
                .append("plantId", getPlantId())
                .append("regionCode", getRegionCode())
                .append("regionName", getRegionName())
                .append("remarks", getRemarks())
                .append("approveStatus", getApproveStatus())
                .append("approveTime", getApproveTime())
                .append("deptId", getDeptId())
                .append("delFlag", getDelFlag())
                .append("createrCode", getCreaterCode())
                .append("createdate", getCreatedate())
                .append("updaterCode", getUpdaterCode())
                .append("updatedate", getUpdatedate())
                .toString();
    }
}
