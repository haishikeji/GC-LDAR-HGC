package com.px.project.check.mapper;

import java.util.List;

import com.px.project.check.domain.TCheckStandardgases;

/**
 * 标准气体Mapper接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface TCheckStandardgasesMapper
{
    /**
     * 查询标准气体
     *
     * @param id 标准气体主键
     * @return 标准气体
     */
    public TCheckStandardgases selectTCheckStandardgasesById(Long id);

    /**
     * 查询标准气体列表
     *
     * @param tCheckStandardgases 标准气体
     * @return 标准气体集合
     */
    public List<TCheckStandardgases> selectTCheckStandardgasesList(TCheckStandardgases tCheckStandardgases);
    public List<TCheckStandardgases> selectAllStandardgases();

    /**
     * 新增标准气体
     *
     * @param tCheckStandardgases 标准气体
     * @return 结果
     */
    public int insertTCheckStandardgases(TCheckStandardgases tCheckStandardgases);

    /**
     * 修改标准气体
     *
     * @param tCheckStandardgases 标准气体
     * @return 结果
     */
    public int updateTCheckStandardgases(TCheckStandardgases tCheckStandardgases);

    /**
     * 删除标准气体
     *
     * @param id 标准气体主键
     * @return 结果
     */
    public int deleteTCheckStandardgasesById(Long id);

    /**
     * 批量删除标准气体
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTCheckStandardgasesByIds(Long[] ids);
}
