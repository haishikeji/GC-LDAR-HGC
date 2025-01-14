package com.px.project.task.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.px.common.annotation.Excel;
import com.px.common.core.domain.BaseEntity;

/**
 * 维修任务对象 t_task_repair
 *
 * @author 品讯科技
 * @date 2024-08
 */
public class TTaskRepair extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 唯一标识id */
    private Long id;

    /** 装置id */
    @Excel(name = "装置id")
    private Long plantId;
    private String plantName;

    /** 任务名称 */
    @Excel(name = "任务名称")
    private String taskName;

    /** 任务编号 */
    @Excel(name = "任务编号")
    private String taskCode;

    /** 任务类型 */
    @Excel(name = "任务类型")
    private String taskType;

    /** 任务起始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "任务起始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 任务截止时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "任务截止时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 接收人 */
    @Excel(name = "接收人")
    @TableField(exist = false)
    private String recipientName;

    private Long recipient;

    /** 维修点数 */
    @Excel(name = "维修点数")
    private String taskNum;

    /** 已维修点数 */
    @Excel(name = "已维修点数")
    private String taskDoneNum;

    /** 未维修点数 */
    @Excel(name = "未维修点数")
    private String taskUndoneNum;

    @TableField(exist = false)
    private Double percentage;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 备注 */
    @Excel(name = "备注")
    private String remarks;

    /** 部门编号 */
    @Excel(name = "部门编号")
    private Long deptId;

    /** 状态  0：正常 ；1：删除 */
    private Integer delFlag;

    /** 创建人 */
    @Excel(name = "创建人")
    private Long createrCode;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdate;

    /** 修改人 */
    @Excel(name = "修改人")
    private Long updaterCode;

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedate;

    @Excel(name = "是否超时")
    @TableField(exist = false)
    private String timeOut;

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setPlantId(Long plantId)
    {
        this.plantId = plantId;
    }

    public Long getPlantId()
    {
        return plantId;
    }
    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
    }

    public String getTaskName()
    {
        return taskName;
    }
    public void setTaskCode(String taskCode)
    {
        this.taskCode = taskCode;
    }

    public String getTaskCode()
    {
        return taskCode;
    }
    public void setTaskType(String taskType)
    {
        this.taskType = taskType;
    }

    public String getTaskType()
    {
        return taskType;
    }
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getStartTime()
    {
        return startTime;
    }
    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }
    public void setRecipient(Long recipient)
    {
        this.recipient = recipient;
    }

    public Long getRecipient()
    {
        return recipient;
    }
    public void setTaskNum(String taskNum)
    {
        this.taskNum = taskNum;
    }

    public String getTaskNum()
    {
        return taskNum;
    }
    public void setTaskDoneNum(String taskDoneNum)
    {
        this.taskDoneNum = taskDoneNum;
    }

    public String getTaskDoneNum()
    {
        return taskDoneNum;
    }
    public void setTaskUndoneNum(String taskUndoneNum)
    {
        this.taskUndoneNum = taskUndoneNum;
    }

    public String getTaskUndoneNum()
    {
        return taskUndoneNum;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public String getRemarks()
    {
        return remarks;
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
            .append("id", getId())
            .append("plantId", getPlantId())
            .append("taskName", getTaskName())
            .append("taskCode", getTaskCode())
            .append("taskType", getTaskType())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("recipient", getRecipient())
            .append("taskNum", getTaskNum())
            .append("taskDoneNum", getTaskDoneNum())
            .append("taskUndoneNum", getTaskUndoneNum())
            .append("status", getStatus())
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
