package com.px.project.check.mapper;

import java.util.List;
import com.px.project.check.domain.TCheckInstrumentRecord;

/**
 * 仪器维护记录Mapper接口
 * 
 * @author 品讯科技
 * @date 2024-08
 */
public interface TCheckInstrumentRecordMapper 
{
    /**
     * 查询仪器维护记录
     * 
     * @param id 仪器维护记录主键
     * @return 仪器维护记录
     */
    public TCheckInstrumentRecord selectTCheckInstrumentRecordById(Long id);

    /**
     * 查询仪器维护记录列表
     * 
     * @param tCheckInstrumentRecord 仪器维护记录
     * @return 仪器维护记录集合
     */
    public List<TCheckInstrumentRecord> selectTCheckInstrumentRecordList(TCheckInstrumentRecord tCheckInstrumentRecord);

    /**
     * 新增仪器维护记录
     * 
     * @param tCheckInstrumentRecord 仪器维护记录
     * @return 结果
     */
    public int insertTCheckInstrumentRecord(TCheckInstrumentRecord tCheckInstrumentRecord);

    /**
     * 修改仪器维护记录
     * 
     * @param tCheckInstrumentRecord 仪器维护记录
     * @return 结果
     */
    public int updateTCheckInstrumentRecord(TCheckInstrumentRecord tCheckInstrumentRecord);

    /**
     * 删除仪器维护记录
     * 
     * @param id 仪器维护记录主键
     * @return 结果
     */
    public int deleteTCheckInstrumentRecordById(Long id);

    /**
     * 批量删除仪器维护记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTCheckInstrumentRecordByIds(Long[] ids);
}
