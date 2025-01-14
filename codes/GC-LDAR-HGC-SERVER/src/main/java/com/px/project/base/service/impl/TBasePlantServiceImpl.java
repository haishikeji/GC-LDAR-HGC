package com.px.project.base.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.px.project.base.mapper.TBasePlantMapper;
import com.px.project.base.domain.TBasePlant;
import com.px.project.base.service.ITBasePlantService;

/**
 * 装置Service业务层处理
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Service
public class TBasePlantServiceImpl implements ITBasePlantService
{
    @Autowired
    private TBasePlantMapper tBasePlantMapper;

    /**
     * 查询装置
     *
     * @param id 装置主键
     * @return 装置
     */
    @Override
    public TBasePlant selectTBasePlantById(Long id)
    {
        return tBasePlantMapper.selectTBasePlantById(id);
    }

    @Override
    public TBasePlant selectTBasePlantByName(String name)
    {
        return tBasePlantMapper.selectTBasePlantByName(name);
    }

    /**
     * 查询装置列表
     *
     * @param tBasePlant 装置
     * @return 装置
     */
    @Override
    public List<TBasePlant> selectTBasePlantList(TBasePlant tBasePlant)
    {
        return tBasePlantMapper.selectTBasePlantList(tBasePlant);
    }
@Override
    public List<TBasePlant> selectAllPlantName()
    {
        return tBasePlantMapper.selectAllPlantName(new TBasePlant());
    }

    /**
     * 新增装置
     *
     * @param tBasePlant 装置
     * @return 结果
     */
    @Override
    public int insertTBasePlant(TBasePlant tBasePlant)
    {
        return tBasePlantMapper.insertTBasePlant(tBasePlant);
    }

    /**
     * 修改装置
     *
     * @param tBasePlant 装置
     * @return 结果
     */
    @Override
    public int updateTBasePlant(TBasePlant tBasePlant)
    {
        return tBasePlantMapper.updateTBasePlant(tBasePlant);
    }

    @Override
    public int updateTBasePlantByPlantIds(TBasePlant tBasePlant)
    {
        return tBasePlantMapper.updateTBasePlantByPlantIds(tBasePlant);
    }

    @Override
    public int disabledPlant(Long id)
    {
        return tBasePlantMapper.disabledPlant(id);
    }

    /**
     * 批量删除装置
     *
     * @param ids 需要删除的装置主键
     * @return 结果
     */
    @Override
    public int deleteTBasePlantByIds(Long[] ids)
    {
        return tBasePlantMapper.deleteTBasePlantByIds(ids);
    }

    /**
     * 删除装置信息
     *
     * @param id 装置主键
     * @return 结果
     */
    @Override
    public int deleteTBasePlantById(Long id)
    {
        return tBasePlantMapper.deleteTBasePlantById(id);
    }
}
