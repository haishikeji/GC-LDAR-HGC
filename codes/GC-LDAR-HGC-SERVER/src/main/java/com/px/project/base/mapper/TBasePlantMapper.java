package com.px.project.base.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.px.common.annotation.DataScopePlant;
import com.px.project.base.domain.TBasePlant;

/**
 * 装置Mapper接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface TBasePlantMapper extends BaseMapper<TBasePlant>
{
    /**
     * 查询装置
     *
     * @param id 装置主键
     * @return 装置
     */
    public TBasePlant selectTBasePlantById(Long id);
    public TBasePlant selectTBasePlantByName(String name);

    /**
     * 查询装置列表
     *
     * @param tBasePlant 装置
     * @return 装置集合
     */
    @DataScopePlant(deptAlias = "d")
    public List<TBasePlant> selectTBasePlantList(TBasePlant tBasePlant);

    @DataScopePlant(deptAlias = "d")
    public List<TBasePlant> selectAllPlantName(TBasePlant tBasePlant);

    /**
     * 新增装置
     *
     * @param tBasePlant 装置
     * @return 结果
     */
    public int insertTBasePlant(TBasePlant tBasePlant);

    /**
     * 修改装置
     *
     * @param tBasePlant 装置
     * @return 结果
     */
    public int updateTBasePlant(TBasePlant tBasePlant);
    public int updateTBasePlantByPlantIds(TBasePlant tBasePlant);

    public int disabledPlant(Long id);

    /**
     * 删除装置
     *
     * @param id 装置主键
     * @return 结果
     */
    public int deleteTBasePlantById(Long id);

    /**
     * 批量删除装置
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTBasePlantByIds(Long[] ids);
}
