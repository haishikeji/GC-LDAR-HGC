package com.px.project.base.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.px.common.annotation.DataScopePlant;
import com.px.project.base.domain.TBaseRegion;
import org.apache.ibatis.annotations.Param;

/**
 * 区域Mapper接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface TBaseRegionMapper extends BaseMapper<TBaseRegion>
{
    /**
     * 查询区域
     *
     * @param id 区域主键
     * @return 区域
     */
    public TBaseRegion selectTBaseRegionById(Long id);
    public TBaseRegion selectTBaseRegionByName(@Param("name") String name,@Param("plantId") Long plantId);

    /**
     * 查询区域列表
     *
     * @param tBaseRegion 区域
     * @return 区域集合
     */
    @DataScopePlant(deptAlias = "d")
    public List<TBaseRegion> selectTBaseRegionList(TBaseRegion tBaseRegion);
    public List<TBaseRegion> selectAllRegionByPlantId(Long plantId);

    /**
     * 新增区域
     *
     * @param tBaseRegion 区域
     * @return 结果
     */
    public int insertTBaseRegion(TBaseRegion tBaseRegion);

    /**
     * 修改区域
     *
     * @param tBaseRegion 区域
     * @return 结果
     */
    public int updateTBaseRegion(TBaseRegion tBaseRegion);
    public int updateTBaseRegionByRegionIds(TBaseRegion tBaseRegion);

    /**
     * 删除区域
     *
     * @param id 区域主键
     * @return 结果
     */
    public int deleteTBaseRegionById(Long id);
    public int disabledRegion(Long id);

    /**
     * 批量删除区域
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTBaseRegionByIds(Long[] ids);
}
