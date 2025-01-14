package com.px.project.check.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.px.project.check.mapper.TCheckStandardgasesMapper;
import com.px.project.check.domain.TCheckStandardgases;
import com.px.project.check.service.ITCheckStandardgasesService;

/**
 * 标准气体Service业务层处理
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Service
public class TCheckStandardgasesServiceImpl implements ITCheckStandardgasesService
{
    @Autowired
    private TCheckStandardgasesMapper tCheckStandardgasesMapper;

    /**
     * 查询标准气体
     *
     * @param id 标准气体主键
     * @return 标准气体
     */
    @Override
    public TCheckStandardgases selectTCheckStandardgasesById(Long id)
    {
        return tCheckStandardgasesMapper.selectTCheckStandardgasesById(id);
    }

    /**
     * 查询标准气体列表
     *
     * @param tCheckStandardgases 标准气体
     * @return 标准气体
     */
    @Override
    public List<TCheckStandardgases> selectTCheckStandardgasesList(TCheckStandardgases tCheckStandardgases)
    {
        return tCheckStandardgasesMapper.selectTCheckStandardgasesList(tCheckStandardgases);
    }

    @Override
    public List<TCheckStandardgases> selectAllStandardgases()
    {
        return tCheckStandardgasesMapper.selectAllStandardgases();
    }

    /**
     * 新增标准气体
     *
     * @param tCheckStandardgases 标准气体
     * @return 结果
     */
    @Override
    public int insertTCheckStandardgases(TCheckStandardgases tCheckStandardgases)
    {
        return tCheckStandardgasesMapper.insertTCheckStandardgases(tCheckStandardgases);
    }

    /**
     * 修改标准气体
     *
     * @param tCheckStandardgases 标准气体
     * @return 结果
     */
    @Override
    public int updateTCheckStandardgases(TCheckStandardgases tCheckStandardgases)
    {
        return tCheckStandardgasesMapper.updateTCheckStandardgases(tCheckStandardgases);
    }

    /**
     * 批量删除标准气体
     *
     * @param ids 需要删除的标准气体主键
     * @return 结果
     */
    @Override
    public int deleteTCheckStandardgasesByIds(Long[] ids)
    {
        return tCheckStandardgasesMapper.deleteTCheckStandardgasesByIds(ids);
    }

    /**
     * 删除标准气体信息
     *
     * @param id 标准气体主键
     * @return 结果
     */
    @Override
    public int deleteTCheckStandardgasesById(Long id)
    {
        return tCheckStandardgasesMapper.deleteTCheckStandardgasesById(id);
    }
}
