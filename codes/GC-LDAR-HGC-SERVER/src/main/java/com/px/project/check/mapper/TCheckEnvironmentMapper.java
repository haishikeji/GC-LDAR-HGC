package com.px.project.check.mapper;

import java.util.List;

import com.px.common.annotation.DataScopePlant;
import com.px.project.check.domain.TCheckEnvironment;

/**
 * 环境本底值记录Mapper接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface TCheckEnvironmentMapper
{
    /**
     * 查询环境本底值记录
     *
     * @param id 环境本底值记录主键
     * @return 环境本底值记录
     */
    public TCheckEnvironment selectTCheckEnvironmentById(Long id);

    /**
     * 查询环境本底值记录列表
     *
     * @param tCheckEnvironment 环境本底值记录
     * @return 环境本底值记录集合
     */
    @DataScopePlant(deptAlias = "d")
    public List<TCheckEnvironment> selectTCheckEnvironmentList(TCheckEnvironment tCheckEnvironment);

    /**
     * 新增环境本底值记录
     *
     * @param tCheckEnvironment 环境本底值记录
     * @return 结果
     */
    public int insertTCheckEnvironment(TCheckEnvironment tCheckEnvironment);

    /**
     * 修改环境本底值记录
     *
     * @param tCheckEnvironment 环境本底值记录
     * @return 结果
     */
    public int updateTCheckEnvironment(TCheckEnvironment tCheckEnvironment);

    /**
     * 删除环境本底值记录
     *
     * @param id 环境本底值记录主键
     * @return 结果
     */
    public int deleteTCheckEnvironmentById(Long id);

    /**
     * 批量删除环境本底值记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTCheckEnvironmentByIds(Long[] ids);

    List<TCheckEnvironment> selectTCheckEnvironmentListByYear();
}
