package com.px.project.base.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.px.common.annotation.DataScopePlant;
import com.px.project.base.domain.TBasePoint;

/**
 * 密封点Mapper接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface TBasePointMapper extends BaseMapper<TBasePoint>
{
    /**
     * 查询密封点
     *
     * @param id 密封点主键
     * @return 密封点
     */
    public TBasePoint selectTBasePointById(Long id);
    public TBasePoint selectTBasePointByGroupCodeAndExtendCode(TBasePoint tBasePoint);

    /**
     * 查询密封点列表
     *
     * @param tBasePoint 密封点
     * @return 密封点集合
     */
    @DataScopePlant(deptAlias = "d")
    public List<TBasePoint> selectTBasePointList(TBasePoint tBasePoint);

    @DataScopePlant(deptAlias = "d")
    public List<TBasePoint> selectTBasePointListByPlan(TBasePoint tBasePoint);
    public List<TBasePoint> selectTBasePointListNotInPlan(TBasePoint tBasePoint);

    /**
     * 新增密封点
     *
     * @param tBasePoint 密封点
     * @return 结果
     */
    public int insertTBasePoint(TBasePoint tBasePoint);

    /**
     * 修改密封点
     *
     * @param tBasePoint 密封点
     * @return 结果
     */
    public int updateTBasePoint(TBasePoint tBasePoint);
    public int updateTBasePointByPointIds(TBasePoint tBasePoint);

    public int updateTBasePointByUploadFile(TBasePoint tBasePoint);

    /**
     * 删除密封点
     *
     * @param id 密封点主键
     * @return 结果
     */
    public int deleteTBasePointById(Long id);
    public int disabledPoint(Long id);

    /**
     * 批量删除密封点
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTBasePointByIds(Long[] ids);
}
