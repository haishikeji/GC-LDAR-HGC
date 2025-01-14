package com.px.project.task.mapper;

import java.util.List;

import com.px.common.annotation.DataScopePlant;
import com.px.project.task.domain.TTaskInspection;

/**
 * 检测任务Mapper接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface TTaskInspectionMapper
{
    /**
     * 查询检测任务
     *
     * @param id 检测任务主键
     * @return 检测任务
     */
    public TTaskInspection selectTTaskInspectionById(Long id);

    /**
     * 查询检测任务列表
     *
     * @param tTaskInspection 检测任务
     * @return 检测任务集合
     */
    @DataScopePlant(deptAlias = "d")
    public List<TTaskInspection> selectTTaskInspectionList(TTaskInspection tTaskInspection);

    /**
     * 新增检测任务
     *
     * @param tTaskInspection 检测任务
     * @return 结果
     */
    public int insertTTaskInspection(TTaskInspection tTaskInspection);

    /**
     * 修改检测任务
     *
     * @param tTaskInspection 检测任务
     * @return 结果
     */
    public int updateTTaskInspection(TTaskInspection tTaskInspection);

    /**
     * 删除检测任务
     *
     * @param id 检测任务主键
     * @return 结果
     */
    public int deleteTTaskInspectionById(Long id);

    /**
     * 批量删除检测任务
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTTaskInspectionByIds(Long[] ids);
    public int updateTTaskInspectionByIds(Long[] ids);
}
