package com.px.project.base.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.px.common.annotation.Excel;
import com.px.common.core.domain.BaseEntity;

/**
 * 装置对象 t_base_plant
 *
 * @author 品讯科技
 * @date 2024-08
 */
public class TBasePlant extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 唯一标识id */
    private Long plantId;

    /** 编号 */
    @Excel(name = "编号")
    private String plantCode;

    /** 名称 */
    @Excel(name = "名称")
    private String plantName;

    /** 加工/生产能力 */
    @Excel(name = "加工/生产能力")
    private String proAbility;

    /** 类别 */
    @Excel(name = "类别",dictType = "plant_type")
    private String plantType;

    /** 描述 */
    @Excel(name = "描述")
    private String remarks;

    /** 审核状态 */
    @Excel(name = "审核状态",dictType = "base_approve_status")
    private Long approveStatus;

    /** 最新申请时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date approveTime;

    /** 部门编号 */
    private Long deptId;

    /** 状态 1 ：正常 ；0：删除 */
    private Integer delFlag;

    /** 创建人 */
    private Long createrCode;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdate;

    /** 修改人 */
    @Excel(name = "维护人")
    private String updater;

    @Excel(name = "公司")
    private String company;

    private Long updaterCode;

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "维护时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedate;

    @TableField(exist = false)
    private Long[] plantIds;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Long[] getPlantIds() {
        return plantIds;
    }

    public void setPlantIds(Long[] plantIds) {
        this.plantIds = plantIds;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public void setPlantId(Long plantId)
    {
        this.plantId = plantId;
    }

    public Long getPlantId()
    {
        return plantId;
    }
    public void setPlantCode(String plantCode)
    {
        this.plantCode = plantCode;
    }

    public String getPlantCode()
    {
        return plantCode;
    }
    public void setPlantName(String plantName)
    {
        this.plantName = plantName;
    }

    public String getPlantName()
    {
        return plantName;
    }
    public void setProAbility(String proAbility)
    {
        this.proAbility = proAbility;
    }

    public String getProAbility()
    {
        return proAbility;
    }
    public void setPlantType(String plantType)
    {
        this.plantType = plantType;
    }

    public String getPlantType()
    {
        return plantType;
    }
    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public String getRemarks()
    {
        return remarks;
    }
    public void setApproveStatus(Long approveStatus)
    {
        this.approveStatus = approveStatus;
    }

    public Long getApproveStatus()
    {
        return approveStatus;
    }
    public void setApproveTime(Date approveTime)
    {
        this.approveTime = approveTime;
    }

    public Date getApproveTime()
    {
        return approveTime;
    }
    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public Long getDeptId()
    {
        return deptId;
    }
    public void setDelFlag(Integer delFlag)
    {
        this.delFlag = delFlag;
    }

    public Integer getDelFlag()
    {
        return delFlag;
    }
    public void setCreaterCode(Long createrCode)
    {
        this.createrCode = createrCode;
    }

    public Long getCreaterCode()
    {
        return createrCode;
    }
    public void setCreatedate(Date createdate)
    {
        this.createdate = createdate;
    }

    public Date getCreatedate()
    {
        return createdate;
    }
    public void setUpdaterCode(Long updaterCode)
    {
        this.updaterCode = updaterCode;
    }

    public Long getUpdaterCode()
    {
        return updaterCode;
    }
    public void setUpdatedate(Date updatedate)
    {
        this.updatedate = updatedate;
    }

    public Date getUpdatedate()
    {
        return updatedate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getPlantId())
            .append("plantNum", getPlantCode())
            .append("plantName", getPlantName())
            .append("proAbility", getProAbility())
            .append("plantType", getPlantType())
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
