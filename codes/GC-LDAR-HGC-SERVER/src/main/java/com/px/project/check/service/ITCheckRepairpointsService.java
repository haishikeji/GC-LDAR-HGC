package com.px.project.check.service;

import java.util.List;
import com.px.project.check.domain.TCheckRepairpoints;

/**
 * 维修点Service接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface ITCheckRepairpointsService
{
    /**
     * 查询维修点
     *
     * @param checkId 维修点主键
     * @return 维修点
     */
    public TCheckRepairpoints selectTCheckRepairpointsByCheckId(Long checkId);

    /**
     * 查询维修点列表
     *
     * @param tCheckRepairpoints 维修点
     * @return 维修点集合
     */
    public List<TCheckRepairpoints> selectTCheckRepairpointsList(TCheckRepairpoints tCheckRepairpoints);

    /**
     * 新增维修点
     *
     * @param tCheckRepairpoints 维修点
     * @return 结果
     */
    public int insertTCheckRepairpoints(TCheckRepairpoints tCheckRepairpoints);
    public int insertTCheckRepairpointsByList(List<TCheckRepairpoints> tCheckRepairpoints);
    public List<TCheckRepairpoints> selectTCheckRepairpointsByIds(Long[] ids);
    public List<TCheckRepairpoints> selectRepairpointsByInspectionId(Long inspectionId);
    /**
     * 修改维修点
     *
     * @param tCheckRepairpoints 维修点
     * @return 结果
     */
    public int updateTCheckRepairpoints(TCheckRepairpoints tCheckRepairpoints);
    public int updateTCheckRepairpointsByCheckIds(TCheckRepairpoints tCheckRepairpoints);
    public int updateTCheckRepairpointsByUpload(TCheckRepairpoints tCheckRepairpoints);
    public int updateApproveStatusByIds(TCheckRepairpoints tCheckRepairpoints);

    /**
     * 批量删除维修点
     *
     * @param checkIds 需要删除的维修点主键集合
     * @return 结果
     */
    public int deleteTCheckRepairpointsByCheckIds(Long[] checkIds);

    /**
     * 删除维修点信息
     *
     * @param checkId 维修点主键
     * @return 结果
     */
    public int deleteTCheckRepairpointsByCheckId(Long checkId);
}
