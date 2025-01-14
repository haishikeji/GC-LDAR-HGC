package com.px.project.check.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.px.common.annotation.Excel;
import com.px.common.core.domain.BaseEntity;

/**
 * 法规项对象 t_check_lawitems
 *
 * @author 品讯科技
 * @date 2024-08
 */
public class TCheckLawitems extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识id
     */
    private Long id;

    /**
     * 法规id
     */
    private Long lawId;

    /**
     * 装置类型
     */
    @Excel(name = "装置类型")
    private String plantType;

    /**
     * 密封点类型
     */
    @Excel(name = "密封点类型")
    private String pointType;

    /**
     * 介质状态
     */
    @Excel(name = "介质状态",readConverterExp = "1=气体,2=轻液体,3=重液体")
    private String mediumType;

    /**
     * 一般泄漏标准
     */
    @Excel(name = "一般泄漏标准")
    private String general;

    /**
     * 严重泄漏标准
     */
    @Excel(name = "严重泄漏标准")
    private String serious;

    /**
     * 监测频次
     */
    @Excel(name = "监测频次")
    private String detectionFrequency;

    /**
     * 首次维修时限
     */
    @Excel(name = "首次维修时限")
    private String stratFix;

    /**
     * 最终维修时限
     */
    @Excel(name = "最终维修时限")
    private String endFix;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remarks;

    /**
     * 部门编号
     */
    @Excel(name = "部门编号")
    private Long deptId;

    /**
     * 状态 1 ：正常 ；0：删除
     */
    private Integer delFlag;

    /**
     * 创建人
     */
    @Excel(name = "创建人")
    private Long createrCode;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdate;

    /**
     * 维护人
     */
    @Excel(name = "维护人")
    private String updater;

    private Long updaterCode;

    /**
     * 维护时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "维护时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedate;

    public Long getLawId() {
        return lawId;
    }

    public void setLawId(Long lawId) {
        this.lawId = lawId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setPlantType(String plantType) {
        this.plantType = plantType;
    }

    public String getPlantType() {
        return plantType;
    }

    public void setPointType(String pointType) {
        this.pointType = pointType;
    }

    public String getPointType() {
        return pointType;
    }

    public void setMediumType(String mediumType) {
        this.mediumType = mediumType;
    }

    public String getMediumType() {
        return mediumType;
    }

    public void setGeneral(String general) {
        this.general = general;
    }

    public String getGeneral() {
        return general;
    }

    public void setSerious(String serious) {
        this.serious = serious;
    }

    public String getSerious() {
        return serious;
    }

    public void setDetectionFrequency(String detectionFrequency) {
        this.detectionFrequency = detectionFrequency;
    }

    public String getDetectionFrequency() {
        return detectionFrequency;
    }

    public void setStratFix(String stratFix) {
        this.stratFix = stratFix;
    }

    public String getStratFix() {
        return stratFix;
    }

    public void setEndFix(String endFix) {
        this.endFix = endFix;
    }

    public String getEndFix() {
        return endFix;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
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

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("lawId", getLawId())
                .append("plantType", getPlantType())
                .append("pointType", getPointType())
                .append("mediumType", getMediumType())
                .append("general", getGeneral())
                .append("serious", getSerious())
                .append("detectionFrequency", getDetectionFrequency())
                .append("stratFix", getStratFix())
                .append("endFix", getEndFix())
                .append("remarks", getRemarks())
                .append("deptId", getDeptId())
                .append("delFlag", getDelFlag())
                .append("createrCode", getCreaterCode())
                .append("createdate", getCreatedate())
                .append("updaterCode", getUpdaterCode())
                .append("updatedate", getUpdatedate())
                .toString();
    }
}
