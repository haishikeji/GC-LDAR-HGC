package com.px.project.check.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.px.common.annotation.RepeatSubmit;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.px.common.annotation.Log;
import com.px.common.core.controller.BaseController;
import com.px.common.core.domain.AjaxResult;
import com.px.common.enums.BusinessType;
import com.px.project.check.domain.TCheckInstrumentRecord;
import com.px.project.check.service.ITCheckInstrumentRecordService;
import com.px.common.utils.poi.ExcelUtil;
import com.px.common.core.page.TableDataInfo;

/**
 * 仪器维护记录Controller
 *
 * @author 品讯科技
 * @date 2024-08
 */
@RestController
@RequestMapping("/check/record")
public class TCheckInstrumentRecordController extends BaseController
{
    @Autowired
    private ITCheckInstrumentRecordService tCheckInstrumentRecordService;

    /**
     * 查询仪器维护记录列表
     */
    @PreAuthorize("@ss.hasPermi('check:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(TCheckInstrumentRecord tCheckInstrumentRecord)
    {
        startPage();
        List<TCheckInstrumentRecord> list = tCheckInstrumentRecordService.selectTCheckInstrumentRecordList(tCheckInstrumentRecord);
        return getDataTable(list);
    }

    /**
     * 导出仪器维护记录列表
     */
    @PreAuthorize("@ss.hasPermi('check:record:export')")
    @Log(title = "仪器维护记录导出", businessType = BusinessType.EXPORT)
    @RepeatSubmit
    @PostMapping("/export")
    public void export(HttpServletResponse response, TCheckInstrumentRecord tCheckInstrumentRecord)
    {
        List<TCheckInstrumentRecord> list = tCheckInstrumentRecordService.selectTCheckInstrumentRecordList(tCheckInstrumentRecord);
        ExcelUtil<TCheckInstrumentRecord> util = new ExcelUtil<TCheckInstrumentRecord>(TCheckInstrumentRecord.class);
        util.exportExcel(response, list, "仪器维护记录数据");
    }

    /**
     * 获取仪器维护记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('check:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tCheckInstrumentRecordService.selectTCheckInstrumentRecordById(id));
    }

    /**
     * 新增仪器维护记录
     */
    @PreAuthorize("@ss.hasPermi('check:record:add')")
    @Log(title = "仪器维护记录新增", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody TCheckInstrumentRecord tCheckInstrumentRecord)
    {
        tCheckInstrumentRecord.setCreaterCode(getUserId());
        return toAjax(tCheckInstrumentRecordService.insertTCheckInstrumentRecord(tCheckInstrumentRecord));
    }

    /**
     * 修改仪器维护记录
     */
    @PreAuthorize("@ss.hasPermi('check:record:edit')")
    @Log(title = "仪器维护记录修改", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody TCheckInstrumentRecord tCheckInstrumentRecord)
    {
        tCheckInstrumentRecord.setUpdaterCode(getUserId());
        return toAjax(tCheckInstrumentRecordService.updateTCheckInstrumentRecord(tCheckInstrumentRecord));
    }

    /**
     * 删除仪器维护记录
     */
    @PreAuthorize("@ss.hasPermi('check:record:remove')")
    @Log(title = "仪器维护记录删除", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tCheckInstrumentRecordService.deleteTCheckInstrumentRecordByIds(ids));
    }
}
