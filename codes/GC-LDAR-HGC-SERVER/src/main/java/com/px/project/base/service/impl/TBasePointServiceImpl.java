package com.px.project.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.px.project.base.mapper.TBasePointMapper;
import com.px.project.base.domain.TBasePoint;
import com.px.project.base.service.ITBasePointService;

/**
 * 密封点Service业务层处理
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Service
public class TBasePointServiceImpl implements ITBasePointService {
    @Autowired
    private TBasePointMapper tBasePointMapper;

    /**
     * 查询密封点
     *
     * @param id 密封点主键
     * @return 密封点
     */
    @Override
    public TBasePoint selectTBasePointById(Long id) {
        return tBasePointMapper.selectTBasePointById(id);
    }

    @Override
    public TBasePoint selectTBasePointByGroupCodeAndExtendCode(TBasePoint tBasePoint) {
        return tBasePointMapper.selectTBasePointByGroupCodeAndExtendCode(tBasePoint);
    }

    /**
     * 查询密封点列表
     *
     * @param tBasePoint 密封点
     * @return 密封点
     */
    @Override
    public List<TBasePoint> selectTBasePointList(TBasePoint tBasePoint) {
        return tBasePointMapper.selectTBasePointList(tBasePoint);
    }

    @Override
    public List<TBasePoint> selectTBasePointListByPlan(TBasePoint tBasePoint) {
        return tBasePointMapper.selectTBasePointListByPlan(tBasePoint);
    }

    @Override
    public List<TBasePoint> selectTBasePointListNotInPlan(TBasePoint tBasePoint) {
        return tBasePointMapper.selectTBasePointListNotInPlan(tBasePoint);
    }

    /**
     * 新增密封点
     *
     * @param tBasePoint 密封点
     * @return 结果
     */
    @Override
    public int insertTBasePoint(TBasePoint tBasePoint) {
        return tBasePointMapper.insertTBasePoint(tBasePoint);
    }

    /**
     * 修改密封点
     *
     * @param tBasePoint 密封点
     * @return 结果
     */
    @Override
    public int updateTBasePoint(TBasePoint tBasePoint) {
        return tBasePointMapper.updateTBasePoint(tBasePoint);
    }

    @Override
    public int updateTBasePointByPointIds(TBasePoint tBasePoint) {
        return tBasePointMapper.updateTBasePointByPointIds(tBasePoint);
    }

    @Override
    public int updateTBasePointByUploadFile(TBasePoint tBasePoint) {
        return tBasePointMapper.updateTBasePointByUploadFile(tBasePoint);
    }

    /**
     * 批量删除密封点
     *
     * @param ids 需要删除的密封点主键
     * @return 结果
     */
    @Override
    public int deleteTBasePointByIds(Long[] ids) {
        return tBasePointMapper.deleteTBasePointByIds(ids);
    }

    /**
     * 删除密封点信息
     *
     * @param id 密封点主键
     * @return 结果
     */
    @Override
    public int deleteTBasePointById(Long id) {
        return tBasePointMapper.deleteTBasePointById(id);
    }

    @Override
    public int disabledPoint(Long id) {
        return tBasePointMapper.disabledPoint(id);
    }
}
