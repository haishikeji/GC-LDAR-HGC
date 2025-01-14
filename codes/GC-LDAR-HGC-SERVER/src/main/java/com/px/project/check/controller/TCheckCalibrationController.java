package com.px.project.check.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSON;
import com.px.common.annotation.RepeatSubmit;
import com.px.common.core.domain.entity.SysDictData;
import com.px.common.utils.file.ExcelUtils;
import com.px.project.check.domain.TCheckInstrument;
import com.px.project.check.service.ITCheckInstrumentService;
import com.px.system.service.ISysDictTypeService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.px.common.annotation.Log;
import com.px.common.core.controller.BaseController;
import com.px.common.core.domain.AjaxResult;
import com.px.common.enums.BusinessType;
import com.px.project.check.domain.TCheckCalibration;
import com.px.project.check.service.ITCheckCalibrationService;
import com.px.common.utils.poi.ExcelUtil;
import com.px.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 检测仪器校准Controller
 *
 * @author 品讯科技
 * @date 2024-08
 */
@RestController
@RequestMapping("/check/calibration")
public class TCheckCalibrationController extends BaseController {
    @Autowired
    private ITCheckCalibrationService tCheckCalibrationService;

    @Autowired
    private ITCheckInstrumentService tCheckInstrumentService;

    @Autowired
    private ISysDictTypeService isysDictTypeService;

    /**
     * 查询检测仪器校准列表
     */
    @PreAuthorize("@ss.hasPermi('check:calibration:list')")
    @GetMapping("/list")
    public TableDataInfo list(TCheckCalibration tCheckCalibration) {
        startPage();
        List<TCheckCalibration> list = tCheckCalibrationService.selectTCheckCalibrationList(tCheckCalibration);
        return getDataTable(list);
    }

    /**
     * 导出检测仪器校准列表
     */
    @PreAuthorize("@ss.hasPermi('check:calibration:export')")
    @Log(title = "检测仪器校准导出", businessType = BusinessType.EXPORT)
    @RepeatSubmit
    @PostMapping("/export")
    public void export(HttpServletResponse response, TCheckCalibration tCheckCalibration) {
        List<TCheckCalibration> list = tCheckCalibrationService.selectTCheckCalibrationList(tCheckCalibration);
        ExcelUtil<TCheckCalibration> util = new ExcelUtil<TCheckCalibration>(TCheckCalibration.class);
        util.exportExcel(response, list, "检测仪器校准数据");
    }

    /**
     * 获取检测仪器校准详细信息
     */
    @PreAuthorize("@ss.hasPermi('check:calibration:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(tCheckCalibrationService.selectTCheckCalibrationById(id));
    }

    /**
     * 新增检测仪器校准
     */
    @PreAuthorize("@ss.hasPermi('check:calibration:add')")
    @Log(title = "检测仪器校准新增", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody TCheckCalibration tCheckCalibration) {
        tCheckCalibration.setCreaterCode(getUserId());
        return toAjax(tCheckCalibrationService.insertTCheckCalibration(tCheckCalibration));
    }

    /**
     * 修改检测仪器校准
     */
    @PreAuthorize("@ss.hasPermi('check:calibration:edit')")
    @Log(title = "检测仪器校准修改", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody TCheckCalibration tCheckCalibration) {
        tCheckCalibration.setUpdaterCode(getUserId());
        return toAjax(tCheckCalibrationService.updateTCheckCalibration(tCheckCalibration));
    }

    /**
     * 删除检测仪器校准
     */
    @PreAuthorize("@ss.hasPermi('check:calibration:remove')")
    @Log(title = "检测仪器校准删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tCheckCalibrationService.deleteTCheckCalibrationByIds(ids));
    }


    @Log(title = "检测仪器标准导入", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping("/importData")
    public AjaxResult importData(@RequestParam("file") MultipartFile file) throws IOException {
        //获取操作人员ID
        Long userId = getUserId();
        //报错行数统计
        List<Integer> failRow = new ArrayList<>();
        Workbook workbook = ExcelUtils.getWorkBook(file);
        Sheet sheet = workbook.getSheetAt(0);
        List<TCheckCalibration> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int rowNum = sheet.getPhysicalNumberOfRows();
        int failNumber = 0;
        for (int i = 1; i < rowNum; i++) {
            try {
                logger.info("读取行数：" + i);
                Row row = sheet.getRow(i);
                int cellNum = row.getLastCellNum();
                TCheckCalibration entity = new TCheckCalibration();
                Date lifespan = null;
                Date checkTime;
                for (int j = 0; j < cellNum; j++) {
                    Cell cell = row.getCell(j);
                    if (cell == null) {
                        continue;
                    }
                    String cellValue = ExcelUtils.getCellValue(cell);
                    logger.info("cellValue:" + cellValue);
                    if (j == 0) {
                        //仪器编号
                        TCheckInstrument tCheckInstrument = new TCheckInstrument();
                        tCheckInstrument.setCode(cellValue);
                        List<TCheckInstrument> tCheckInstruments = tCheckInstrumentService.selectTCheckInstrumentList(tCheckInstrument);
                        if (CollectionUtils.isEmpty(tCheckInstruments)) {
                            return AjaxResult.success("第 " + (i + 1) + " 行：未找到对应仪器！请检查仪器编号无误后重新提交！", 0);
                        }
                        entity.setInstrumentId(tCheckInstruments.get(0).getId());
                    } else if (j == 4) {
                        // 标气有效期
                        lifespan = sdf.parse(cellValue);
                    } else if (j == 6) {
                        //标气校准值
                        entity.setStandardgasesCalibration(cellValue);
                    } else if (j == 7) {
                        //零气浓度值
                        entity.setZerogasConcentration(cellValue);
                    } else if (j == 8) {
                        // 流量检查
                        entity.setFlowCheck(cellValue);
                    } else if (j == 9) {
                        // 零气校准值
                        entity.setZerogasCalibration(cellValue);
                    } else if (j == 10) {
                        // 气体钢瓶压力
                        entity.setPressure(cellValue);
                    } else if (j == 11) {
                        // 漂移核查值
                        entity.setDriftCheck(cellValue);// 设备id
                    } else if (j == 12) {
                        // 校准人员
                        entity.setCalibrationUser(cellValue);
                    } else if (j == 13) {
                        // 描述
                        entity.setRemarks(cellValue);
                    } else if (j == 14) {
                        // 校准日期，如果在标气有效期之后，不添加
                        checkTime = sdf.parse(cellValue);
                        if (lifespan!=null&&checkTime.after(lifespan)) {
                            return AjaxResult.success("第 " + (i + 1) + " 行：校准日期超过标气有效期！请检查填写无误后重新提交！", 0);
                        }
                        entity.setCalibrationTime(sdf.parse(cellValue));
                    } else if (j == 15) {
                        // 过滤片检查
                        for (SysDictData dictData : isysDictTypeService.selectDictDataByType("yes_no")) {
                            if (dictData.getDictLabel().equals(cellValue)) {
                                entity.setFilterCheck(dictData.getDictValue());
                                break;
                            }
                        }
                    } else if (j == 16) {
                        //气密性检查
                        for (SysDictData dictData : isysDictTypeService.selectDictDataByType("airtightness_check")) {
                            if (dictData.getDictLabel().equals(cellValue)) {
                                entity.setAirtightnessCheck(dictData.getDictValue());
                                break;
                            }
                        }
                    }
                }
                entity.setCreaterCode(userId);
                logger.info("entity:" + entity);
                list.add(entity);
            } catch (Exception e) {
                failNumber++;
                logger.info("e:" + JSON.toJSONString(e));
                failRow.add(i + 1);
            }
        }
        int successNumber = 0;
        int failNum = 0;
        for (TCheckCalibration t : list
        ) {
            failNum++;
            try {
                //进行数据更新
                add(t);
                successNumber++;
            } catch (Exception e) {
                failNumber++;
                logger.info("e:" + e);
                failRow.add(failNum + 1);
            }
        }
        logger.info("list:" + JSON.toJSONString(list));
        logger.info("successNumber:" + successNumber);
        logger.info("failNumber:" + failNumber);
        logger.info("failRow:" + failRow);
        return AjaxResult.success(String.valueOf(successNumber), failRow);
    }
}
