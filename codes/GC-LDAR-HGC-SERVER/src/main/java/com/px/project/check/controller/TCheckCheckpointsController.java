package com.px.project.check.controller;

import com.alibaba.fastjson2.JSON;
import com.px.common.annotation.Log;
import com.px.common.annotation.RepeatSubmit;
import com.px.common.core.controller.BaseController;
import com.px.common.core.domain.AjaxResult;
import com.px.common.core.page.TableDataInfo;
import com.px.common.enums.BusinessType;
import com.px.common.utils.DateUtils;
import com.px.common.utils.file.ExcelUtils;
import com.px.common.utils.poi.ExcelUtil;
import com.px.project.base.service.ITBasePointService;
import com.px.project.check.domain.TCheckCheckpoints;
import com.px.project.check.domain.TCheckEnvironment;
import com.px.project.check.domain.TCheckLawitems;
import com.px.project.check.mapper.TCheckCheckpointsMapper;
import com.px.project.check.mapper.TCheckEnvironmentMapper;
import com.px.project.check.service.ITCheckCheckpointsService;
import com.px.project.check.service.ITCheckEnvironmentService;
import com.px.project.check.service.ITCheckLawitemsService;
import com.px.project.check.service.ITCheckRepairpointsService;
import com.px.project.task.service.ITTaskInspectionService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 检测点Controller
 *
 * @author 品讯科技
 * @date 2024-08
 */
@RestController
@RequestMapping("/check/checkpoints")
public class TCheckCheckpointsController extends BaseController {
    @Autowired
    private ITCheckCheckpointsService tCheckCheckpointsService;
    @Resource
    private TCheckCheckpointsMapper tCheckCheckpointsMapper;
    @Autowired
    private ITCheckEnvironmentService tCheckEnvironmentService;
    @Resource
    private TCheckEnvironmentMapper tCheckEnvironmentMapper;
    @Autowired
    private ITBasePointService tBasePointService;

    @Autowired
    private ITTaskInspectionService taskInspectionService;

    @Autowired
    private ITCheckLawitemsService tCheckLawitemsService;

    @Autowired
    private ITCheckRepairpointsService tCheckRepairpointsService;


    /**
     * 查询检测点列表
     */
//    @PreAuthorize("@ss.hasPermi('check:checkpoints:list')")
    @GetMapping("/list")
    public TableDataInfo list(TCheckCheckpoints tCheckCheckpoints) {
        startPage();
        List<TCheckCheckpoints> list = tCheckCheckpointsService.selectTCheckCheckpointsList(tCheckCheckpoints);
        return getDataTable(list);
    }

    /**
     * 导出检测点列表
     */
//    @PreAuthorize("@ss.hasPermi('check:checkpoints:export')")
    @Log(title = "检测点导出", businessType = BusinessType.EXPORT)
    @RepeatSubmit
    @PostMapping("/export")
    public void export(HttpServletResponse response, TCheckCheckpoints tCheckCheckpoints) {
        List<TCheckCheckpoints> list = tCheckCheckpointsService.selectTCheckCheckpointsList(tCheckCheckpoints);
        ExcelUtil<TCheckCheckpoints> util = new ExcelUtil<TCheckCheckpoints>(TCheckCheckpoints.class);
        util.exportExcel(response, list, "检测点数据");
    }

    /**
     * 获取检测点详细信息
     */
//    @PreAuthorize("@ss.hasPermi('check:checkpoints:query')")
    @GetMapping(value = "/{checkId}")
    public AjaxResult getInfo(@PathVariable("checkId") Long checkId) {
        return AjaxResult.success(tCheckCheckpointsService.selectTCheckCheckpointsByCheckId(checkId));
    }

    /**
     * 新增检测点
     */
//    @PreAuthorize("@ss.hasPermi('check:checkpoints:add')")
    @Log(title = "检测点新增", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody TCheckCheckpoints tCheckCheckpoints) {
        return toAjax(tCheckCheckpointsService.insertTCheckCheckpoints(tCheckCheckpoints));
    }

    /**
     * 修改检测点
     */
//    @PreAuthorize("@ss.hasPermi('check:checkpoints:edit')")
    @Log(title = "检测点修改", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody TCheckCheckpoints tCheckCheckpoints) {
        return toAjax(tCheckCheckpointsService.updateTCheckCheckpointsByCheckIds(tCheckCheckpoints));
    }

//    @PreAuthorize("@ss.hasPermi('check:checkpoints:edit')")
    @Log(title = "审核检测结果", businessType = BusinessType.APPROVE)
    @RepeatSubmit
    @PostMapping("/toApprove")
    public AjaxResult toApprove(@RequestBody TCheckCheckpoints tCheckCheckpoints) {
        return tCheckCheckpointsService.toApprove(tCheckCheckpoints);
    }

//    @PreAuthorize("@ss.hasPermi('check:checkpoints:edit')")
    @Log(title = "审核所有检测结果", businessType = BusinessType.APPROVE)
    @RepeatSubmit
    @PostMapping("/approveAll")
    public AjaxResult approveAll(@RequestBody TCheckCheckpoints tCheckCheckpoints) {
        return tCheckCheckpointsService.approveAll(tCheckCheckpoints);
    }

    /**
     * 删除检测点
     */
//    @PreAuthorize("@ss.hasPermi('check:checkpoints:remove')")
    @Log(title = "检测点删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/{checkIds}")
    public AjaxResult remove(@PathVariable Long[] checkIds) {
        return toAjax(tCheckCheckpointsService.deleteTCheckCheckpointsByCheckIds(checkIds));
    }

    @Log(title = "检测数据录入", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping("/importData/{inspectionId}")
    public AjaxResult importData(@RequestParam("file") MultipartFile file, @PathVariable Long inspectionId) {
        List<String> resultList = new ArrayList<>();
        //获取操作人员ID
        Long userId = getUserId();
        //报错行数统计
        CopyOnWriteArrayList<Integer> failRow = new CopyOnWriteArrayList<>();
        Workbook workbook = ExcelUtils.getWorkBook(file);
        Sheet sheet = workbook.getSheetAt(0);
        List<TCheckCheckpoints> list = new ArrayList<>();
        int rowNum = sheet.getPhysicalNumberOfRows();
        //线程池
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        final CountDownLatch latch = new CountDownLatch(rowNum - 1); //相同线程数量的计数
        List<TCheckEnvironment> tCheckEnvironments = tCheckEnvironmentMapper.selectTCheckEnvironmentListByYear();

        List<TCheckLawitems> tCheckLawitems = tCheckLawitemsService.selectTCheckLawitemsByLawStatus();
        if(CollectionUtils.isEmpty(tCheckLawitems)){
            return AjaxResult.error("未找到匹配的法规项！请检查法规项后重试！");
        }
        AtomicInteger successNumber = new AtomicInteger();
        for (int i = 1; i < rowNum; i++) {
            int finalI = i;
            executorService.execute(() -> {
                try {
                    logger.info("读取行数：" + finalI);
                    Row row = sheet.getRow(finalI);
                    int cellNum = row.getLastCellNum();
                    TCheckCheckpoints entity = new TCheckCheckpoints();
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
                            //检测值
                            entity.setTestValue(cellValue);
                        } else if (j == 4) {
                            // 泄漏部位
                            entity.setLeakagePosition(cellValue);
                        } else if (j == 5) {
                            //检测仪器编号
                            entity.setInstrumentCode(cellValue);
                        } else if (j == 6) {
                            // 检测人员
                            entity.setChecker(cellValue);
                        } else if (j == 7) {
                            // 检测日期
                            entity.setCheckDate(new SimpleDateFormat(DateUtils.getDateFormat(cellValue)).parse(cellValue));
                        }
                    }
                    entity.setUpdatedate(new Date());
                    entity.setUpdaterCode(userId);
                    TCheckEnvironment environment = new TCheckEnvironment();
                    environment.setPlantName(entity.getPlantName());
                    environment.setInstrumentCode(entity.getInstrumentCode());
                    environment.setTestTime(entity.getCheckDate());
//                    List<TCheckEnvironment> tCheckEnvironments = tCheckEnvironmentMapper.selectTCheckEnvironmentList(environment);
                    TCheckEnvironment tCheckEnvironment = new TCheckEnvironment();
                    for (TCheckEnvironment t : tCheckEnvironments
                    ) {
                        if (t.getTestTime().compareTo(entity.getCheckDate())==0 && t.getInstrumentCode().equals(entity.getInstrumentCode()) && t.getPlantName().equals(entity.getPlantName())) {
                            tCheckEnvironment = t;
                            break;
                        }
                    }
                    if (tCheckEnvironment.getId() == null) {
                        logger.info("环境本底值数据不存在");
                        failRow.add(finalI + 1);
                        resultList.add("第"+(finalI + 1)+"行导入失败，查询不到当天的环境本底值数据");
                        throw new InterruptedException();//中止线程
                    } else {
                        String netTest;
                        try {
                            BigDecimal back = new BigDecimal(tCheckEnvironment.getBackgroundValue());// 当天的环境本底值
                            BigDecimal test = new BigDecimal(entity.getTestValue());// 当天检测值
                            // 计算净检测值
                            netTest = test.subtract(back).setScale(2, RoundingMode.HALF_UP).toString();
                            netTest = new BigDecimal(netTest).compareTo(BigDecimal.ZERO) < 0 ? "0" : netTest;
                            entity.setNetTestValue(netTest);
                        } catch (Exception e) {
                            logger.info("计算本底值错误");
                            failRow.add(finalI + 1);
                            resultList.add("第"+(finalI + 1)+"行导入失败，计算净检测值出错");
                            throw new InterruptedException();//中止线程
                        }
                        entity.setInstrumentCode(tCheckEnvironment.getInstrumentCode());// 仪器编号
                        entity.setInstrumentId(tCheckEnvironment.getInstrumentId());// 仪器id
                        entity.setInspectionId(inspectionId);// 任务id
                        entity.setApproveStatus(0l);// 任务id
//                        tCheckCheckpointsMapper.updateTCheckCheckpointsByUpload(entity); //更新检测点数据
                        TCheckCheckpoints tCheckCheckpoint = tCheckCheckpointsMapper.selectTCheckCheckpointByEntity(entity);
                        for (TCheckLawitems tCheckLawitem : tCheckLawitems) {
                            if (tCheckLawitem.getPlantType().equals(tCheckCheckpoint.getPlantType())
                                    && tCheckLawitem.getPointType().equals(tCheckCheckpoint.getPointType())
                                    && tCheckLawitem.getMediumType().equals(tCheckCheckpoint.getMediumType())) {
                                if (new BigDecimal(netTest).compareTo(new BigDecimal(tCheckLawitem.getSerious())) >= 0) {
                                    //如果净检测值大于等于当前匹配法规的严重泄漏标准
                                    entity.setLeakageDegree("3");
                                } else if (new BigDecimal(netTest).compareTo(new BigDecimal(tCheckLawitem.getGeneral())) >= 0) {
                                    //如果净检测值大于等于当前匹配法规的一般泄漏标准
                                    entity.setLeakageDegree("2");
                                } else {
                                    //如果净检测值小于当前匹配法规的一般泄漏标准
                                    entity.setLeakageDegree("1");
                                }
                            }
                        }
                        int upload = tCheckCheckpointsMapper.updateTCheckCheckpointsByUpload(entity); //更新检测点数据
                        if (upload == 1) {
                            successNumber.getAndIncrement();
                        } else {
                            logger.info("数据不存在");
                            failRow.add(finalI + 1);
                            resultList.add("第"+(finalI + 1)+"行导入失败，查询不到对应的检测点数据");
                            throw new InterruptedException();//中止线程
                        }
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
        return AjaxResult.success(String.valueOf(successNumber.get()), resultList);
    }


}
