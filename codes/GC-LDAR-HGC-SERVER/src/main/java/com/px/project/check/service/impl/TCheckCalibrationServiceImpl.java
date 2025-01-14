package com.px.project.check.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.px.project.check.mapper.TCheckCalibrationMapper;
import com.px.project.check.domain.TCheckCalibration;
import com.px.project.check.service.ITCheckCalibrationService;

/**
 * 检测仪器校准Service业务层处理
 * 
 * @author 品讯科技
 * @date 2024-08
 */
@Service
public class TCheckCalibrationServiceImpl implements ITCheckCalibrationService 
{
    @Autowired
    private TCheckCalibrationMapper tCheckCalibrationMapper;

    /**
     * 查询检测仪器校准
     * 
     * @param id 检测仪器校准主键
     * @return 检测仪器校准
     */
    @Override
    public TCheckCalibration selectTCheckCalibrationById(Long id)
    {
        return tCheckCalibrationMapper.selectTCheckCalibrationById(id);
    }

    /**
     * 查询检测仪器校准列表
     * 
     * @param tCheckCalibration 检测仪器校准
     * @return 检测仪器校准
     */
    @Override
    public List<TCheckCalibration> selectTCheckCalibrationList(TCheckCalibration tCheckCalibration)
    {
        return tCheckCalibrationMapper.selectTCheckCalibrationList(tCheckCalibration);
    }

    /**
     * 新增检测仪器校准
     * 
     * @param tCheckCalibration 检测仪器校准
     * @return 结果
     */
    @Override
    public int insertTCheckCalibration(TCheckCalibration tCheckCalibration)
    {
        return tCheckCalibrationMapper.insertTCheckCalibration(tCheckCalibration);
    }

    /**
     * 修改检测仪器校准
     * 
     * @param tCheckCalibration 检测仪器校准
     * @return 结果
     */
    @Override
    public int updateTCheckCalibration(TCheckCalibration tCheckCalibration)
    {
        return tCheckCalibrationMapper.updateTCheckCalibration(tCheckCalibration);
    }

    /**
     * 批量删除检测仪器校准
     * 
     * @param ids 需要删除的检测仪器校准主键
     * @return 结果
     */
    @Override
    public int deleteTCheckCalibrationByIds(Long[] ids)
    {
        return tCheckCalibrationMapper.deleteTCheckCalibrationByIds(ids);
    }

    /**
     * 删除检测仪器校准信息
     * 
     * @param id 检测仪器校准主键
     * @return 结果
     */
    @Override
    public int deleteTCheckCalibrationById(Long id)
    {
        return tCheckCalibrationMapper.deleteTCheckCalibrationById(id);
    }
}
