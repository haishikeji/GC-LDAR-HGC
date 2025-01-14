package com.px.project.check.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.px.common.annotation.Excel;
import com.px.common.core.domain.BaseEntity;

/**
 * 法规对象 t_check_laws
 *
 * @author 品讯科技
 * @date 2024-08
 */
public class TCheckLaws extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 唯一标识id */
    private Long id;

    /** 法规编号 */
    @Excel(name = "法规编号")
    private String code;

    /** 法规名称 */
    @Excel(name = "法规名称")
    private String name;

    /** 法规类型 */
    @Excel(name = "法规类型")
    private String type;

    /** 匹配状态 */
    @Excel(name = "匹配状态", readConverterExp="0=未匹配,1=匹配中")
    private String status;

    /** 匹配开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "匹配开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date starttime;

    /** 备注 */
    @Excel(name = "备注")
    private String remarks;

    /** 部门编号 */
    private Long deptId;

    /** 状态 1 ：正常 ；0：删除 */
    private Integer delFlag;

    /** 创建人 */
    private Long createrCode;

    /** 创建时间 */
    private Date createdate;

    /** 维护人 */

    private Long updaterCode;
    private String updater;

    /** 维护时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updatedate;

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setCode(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setStarttime(Date starttime)
    {
        this.starttime = starttime;
    }

    public Date getStarttime()
    {
        return starttime;
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
            .append("code", getCode())
            .append("name", getName())
            .append("type", getType())
            .append("status", getStatus())
            .append("starttime", getStarttime())
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
