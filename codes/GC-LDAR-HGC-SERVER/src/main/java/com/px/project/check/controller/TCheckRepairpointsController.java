package com.px.project.check.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSON;
import com.px.common.utils.DateUtils;
import com.px.common.annotation.RepeatSubmit;
import com.px.common.utils.StringUtils;
import com.px.common.utils.file.ExcelUtils;
import com.px.project.check.domain.TCheckCheckpoints;
import com.px.project.check.service.ITCheckCheckpointsService;
import com.px.project.task.domain.TTaskRepair;
import com.px.project.task.service.ITTaskRepairService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.px.common.annotation.Log;
import com.px.common.core.controller.BaseController;
import com.px.common.core.domain.AjaxResult;
import com.px.common.enums.BusinessType;
import com.px.project.check.domain.TCheckRepairpoints;
import com.px.project.check.service.ITCheckRepairpointsService;
import com.px.common.utils.poi.ExcelUtil;
import com.px.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 维修点Controller
 *
 * @author 品讯科技
 * @date 2024-08
 */
@RestController
@RequestMapping("/check/repairpoints")
public class TCheckRepairpointsController extends BaseController {
    @Autowired
    private ITCheckRepairpointsService tCheckRepairpointsService;

    @Autowired
    private ITTaskRepairService taskRepairService;

    @Autowired
    private ITCheckCheckpointsService tCheckCheckpointsService;

    /**
     * 查询维修点列表
     */
//    @PreAuthorize("@ss.hasPermi('check:repairpoints:list')")
    @GetMapping("/list")
    public TableDataInfo list(TCheckRepairpoints tCheckRepairpoints) {
        startPage();
        List<TCheckRepairpoints> list = tCheckRepairpointsService.selectTCheckRepairpointsList(tCheckRepairpoints);
        return getDataTable(list);
    }

    /**
     * 导出维修点列表
     */
//    @PreAuthorize("@ss.hasPermi('check:repairpoints:export')")
    @Log(title = "维修点导出", businessType = BusinessType.EXPORT)
    @RepeatSubmit
    @PostMapping("/export")
    public void export(HttpServletResponse response, TCheckRepairpoints tCheckRepairpoints) {
        List<TCheckRepairpoints> list = tCheckRepairpointsService.selectTCheckRepairpointsList(tCheckRepairpoints);
        ExcelUtil<TCheckRepairpoints> util = new ExcelUtil<TCheckRepairpoints>(TCheckRepairpoints.class);
        util.exportExcel(response, list, "维修点数据");
    }

    /**
     * 获取维修点详细信息
     */
//    @PreAuthorize("@ss.hasPermi('check:repairpoints:query')")
    @GetMapping(value = "/{checkId}")
    public AjaxResult getInfo(@PathVariable("checkId") Long checkId) {
        return AjaxResult.success(tCheckRepairpointsService.selectTCheckRepairpointsByCheckId(checkId));
    }

    @GetMapping(value = "/byInspectionId/{inspectionId}")
    public TableDataInfo selectRepairpointsByInspectionId(@PathVariable("inspectionId") Long inspectionId) {
        startPage();
        List<TCheckRepairpoints> list = tCheckRepairpointsService.selectRepairpointsByInspectionId(inspectionId);
        return getDataTable(list);
    }

//    @PreAuthorize("@ss.hasPermi('check:checkpoints:edit')")
    @Log(title = "审核维修结果", businessType = BusinessType.APPROVE)
    @RepeatSubmit
    @PutMapping("/toApprove")
    public AjaxResult toApprove(@RequestBody TCheckRepairpoints tCheckRepairpoints) {
        int i = tCheckRepairpointsService.updateApproveStatusByIds(tCheckRepairpoints);
        List<TCheckCheckpoints> tCheckCheckpoints = new ArrayList<>();
        if (i > 0) {
            if (2 == tCheckRepairpoints.getApproveStatus()) {
                TTaskRepair tTaskRepair = taskRepairService.selectTTaskRepairById(tCheckRepairpoints.getRepairId());
                tTaskRepair.setTaskDoneNum(new BigDecimal(StringUtils.isNotEmpty(tTaskRepair.getTaskDoneNum()) ? tTaskRepair.getTaskDoneNum() : "0").add(new BigDecimal(tCheckRepairpoints.getCheckIds().length)).toString());
                tTaskRepair.setTaskUndoneNum(new BigDecimal(tTaskRepair.getTaskNum()).subtract(new BigDecimal(tTaskRepair.getTaskDoneNum())).toString());
                taskRepairService.updateTTaskRepair(tTaskRepair);
                for (TCheckRepairpoints point : tCheckRepairpointsService.selectTCheckRepairpointsByIds(tCheckRepairpoints.getCheckIds())) {
                    TCheckCheckpoints points = new TCheckCheckpoints();
                    points.setMediumType(point.getMediumType());
                    points.setPlantType(point.getPlantType());
                    points.setPlantId(point.getPlantId());
                    points.setRegionId(point.getRegionId());
                    points.setDevId(point.getDevId());
                    points.setPointId(point.getPointId());
                    points.setPlantName(point.getPlantName());
                    points.setRegionName(point.getRegionName());
                    points.setLayer(point.getLayer());
                    points.setDevName(point.getDevName());
                    points.setDevCode(point.getDevCode());
                    points.setGroupCode(point.getGroupCode());
                    points.setExtendCode(point.getExtendCode());
                    points.setPointType(point.getPointType());
                    points.setCreaterCode(getUserId());
                    points.setCreatedate(new Date());
                    points.setUpdaterCode(getUserId());
                    points.setUpdatedate(new Date());
                    points.setRepairId(point.getCheckId());
                    tCheckCheckpoints.add(points);
                }
                if (CollectionUtils.isNotEmpty(tCheckCheckpoints)) {
                    tCheckCheckpointsService.insertTCheckCheckpointsByList(tCheckCheckpoints);
                }
            }
        } else
            return AjaxResult.error();
        return AjaxResult.success();
    }

    /**
     * 新增维修点
     */
//    @PreAuthorize("@ss.hasPermi('check:repairpoints:add')")
    @Log(title = "维修点新增", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody TCheckRepairpoints tCheckRepairpoints) {
        return toAjax(tCheckRepairpointsService.insertTCheckRepairpoints(tCheckRepairpoints));
    }

    /**
     * 修改维修点
     */
//    @PreAuthorize("@ss.hasPermi('check:repairpoints:edit')")
    @Log(title = "维修点修改", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody TCheckRepairpoints tCheckRepairpoints) {
        return toAjax(tCheckRepairpointsService.updateTCheckRepairpointsByCheckIds(tCheckRepairpoints));
    }

    /**
     * 删除维修点
     */
//    @PreAuthorize("@ss.hasPermi('check:repairpoints:remove')")
    @Log(title = "维修点删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/{checkIds}")
    public AjaxResult remove(@PathVariable Long[] checkIds) {
        return toAjax(tCheckRepairpointsService.deleteTCheckRepairpointsByCheckIds(checkIds));
    }

    @Log(title = "维修数据录入", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping("/importData/{repairId}")
    public AjaxResult importData(@RequestParam("file") MultipartFile file, @PathVariable Long repairId) {
        ArrayList<String> resultList = new ArrayList<>();
        //获取操作人员ID
        Long userId = getUserId();
        //报错行数统计
        CopyOnWriteArrayList<Integer> failRow = new CopyOnWriteArrayList<>();
        Workbook workbook = ExcelUtils.getWorkBook(file);
        Sheet sheet = workbook.getSheetAt(0);
        List<TCheckRepairpoints> list = new ArrayList<>();
        int rowNum = sheet.getPhysicalNumberOfRows();
        //线程池
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        final CountDownLatch latch = new CountDownLatch(rowNum - 1); //相同线程数量的计数
        AtomicInteger failNumber = new AtomicInteger();

        AtomicInteger successNumber = new AtomicInteger();
        for (int i = 1; i < rowNum; i++) {
            int finalI = i;
            executorService.execute(() -> {
                try {
                    logger.info("读取行数：" + finalI);
                    Row row = sheet.getRow(finalI);
                    int cellNum = row.getLastCellNum();
                    TCheckRepairpoints entity = new TCheckRepairpoints();
                    for (int j = 0; j < cellNum; j++) {
                        Cell cell = row.getCell(j);
                        if (cell == null) {
                            continue;
                        }
                        String cellValue = ExcelUtils.getCellValue(cell);
                        logger.info("cellValue:" + cellValue);
                        if (j == 0) {
                            //装置名称
                            entity.setPlantName(cellValue);
                        } else if (j == 1) {
                            //群组编号
                            entity.setGroupCode(cellValue);
                        } else if (j == 2) {
                            // 密封点扩展编码
                            entity.setExtendCode(cellValue);
                        } else if (j == 3) {
                            //维修方法
                            entity.setRepairFunc(cellValue);
                        } else if (j == 4) {
                            // 维修人员
                            entity.setRepairer(cellValue);
                        } else if (j == 5) {
                            // 维修日期
                            entity.setRepairDate(new SimpleDateFormat(DateUtils.getDateFormat(cellValue)).parse(cellValue));
                        } else if (j == 6) {
                            // 描述
                            entity.setRemarks(cellValue);
                        }
                    }
                    entity.setUpdatedate(new Date());
                    entity.setUpdaterCode(userId);
                    entity.setRepairId(repairId);// 任务id
                    entity.setApproveStatus(0l);// 任务id
                    int upload = tCheckRepairpointsService.updateTCheckRepairpointsByUpload(entity); //更新检测点数据
                    if (upload == 1) {
                        successNumber.getAndIncrement();
                    } else {
                        logger.info("数据不存在");
                        failRow.add(finalI + 1);
                        resultList.add("第"+(finalI + 1)+"行导入失败，查询不到对应的维修点数据");
                        throw new InterruptedException();//中止线程
                    }
                } catch (InterruptedException e) {
                    logger.info("中止线程" + Thread.currentThread().getName());
                } catch (Exception e) {
                    failRow.add(finalI + 1);
                    resultList.add("第"+(finalI + 1)+"行导入失败");
                    e.printStackTrace();
                } finally {
                    latch.countDown(); //线程计数
                }
            });
        }
        try {
            latch.await(); //线程计数完毕后继续执行
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        logger.info("list:" + JSON.toJSONString(list));
        logger.info("successNumber:" + successNumber);
        logger.info("failNumber:" + failRow.size());
        logger.info("failRow:" + failRow);
        return AjaxResult.success(String.valueOf(successNumber.get()), failRow);
    }
}
