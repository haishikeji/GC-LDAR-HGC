package com.px.project.base.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.px.project.base.mapper.TBaseDeviceMapper;
import com.px.project.base.domain.TBaseDevice;
import com.px.project.base.service.ITBaseDeviceService;

/**
 * 设备/管线Service业务层处理
 *
 * @author 品讯科技
 * @date 2024-08
 */
@Service
public class TBaseDeviceServiceImpl implements ITBaseDeviceService
{
    @Autowired
    private TBaseDeviceMapper tBaseDeviceMapper;

    /**
     * 查询设备/管线
     *
     * @param id 设备/管线主键
     * @return 设备/管线
     */
    @Override
    public TBaseDevice selectTBaseDeviceById(Long id)
    {
        return tBaseDeviceMapper.selectTBaseDeviceById(id);
    }

    /**
     * 查询设备/管线列表
     *
     * @param tBaseDevice 设备/管线
     * @return 设备/管线
     */
    @Override
    public List<TBaseDevice> selectTBaseDeviceList(TBaseDevice tBaseDevice)
    {
        return tBaseDeviceMapper.selectTBaseDeviceList(tBaseDevice);
    }
    @Override
    public List<TBaseDevice> selectAllDeviceByRegionId(Long regionId)
    {
        return tBaseDeviceMapper.selectAllDeviceByRegionId(regionId);
    }
    @Override
    public TBaseDevice selectDeviceByPlantRegionAndDevice(TBaseDevice tBaseDevice)
    {
        return tBaseDeviceMapper.selectDeviceByPlantRegionAndDevice(tBaseDevice);
    }

    /**
     * 新增设备/管线
     *
     * @param tBaseDevice 设备/管线
     * @return 结果
     */
    @Override
    public int insertTBaseDevice(TBaseDevice tBaseDevice)
    {
        return tBaseDeviceMapper.insertTBaseDevice(tBaseDevice);
    }

    /**
     * 修改设备/管线
     *
     * @param tBaseDevice 设备/管线
     * @return 结果
     */
    @Override
    public int updateTBaseDevice(TBaseDevice tBaseDevice)
    {
        return tBaseDeviceMapper.updateTBaseDevice(tBaseDevice);
    }
    @Override
    public int updateTBaseDeviceByDevIds(TBaseDevice tBaseDevice)
    {
        return tBaseDeviceMapper.updateTBaseDeviceByDevIds(tBaseDevice);
    }

    /**
     * 批量删除设备/管线
     *
     * @param ids 需要删除的设备/管线主键
     * @return 结果
     */
    @Override
    public int deleteTBaseDeviceByIds(Long[] ids)
    {
        return tBaseDeviceMapper.deleteTBaseDeviceByIds(ids);
    }

    /**
     * 删除设备/管线信息
     *
     * @param id 设备/管线主键
     * @return 结果
     */
    @Override
    public int deleteTBaseDeviceById(Long id)
    {
        return tBaseDeviceMapper.deleteTBaseDeviceById(id);
    }

    @Override
    public int disabledDevice(Long id)
    {
        return tBaseDeviceMapper.disabledDevice(id);
    }
}
