package com.px.project.base.controller;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.px.common.annotation.Log;
import com.px.common.annotation.RepeatSubmit;
import com.px.common.core.controller.BaseController;
import com.px.common.core.domain.AjaxResult;
import com.px.common.core.page.TableDataInfo;
import com.px.common.enums.BusinessType;
import com.px.common.utils.StringUtils;
import com.px.common.utils.file.ExcelUtils;
import com.px.common.utils.poi.ExcelUtil;
import com.px.project.base.domain.TBasePlant;
import com.px.project.base.domain.TBaseRegion;
import com.px.project.base.mapper.TBaseRegionMapper;
import com.px.project.base.service.ITBasePlantService;
import com.px.project.base.service.ITBaseRegionService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 区域Controller
 *
 * @author 品讯科技
 * @date 2024-08
 */
@RestController
@RequestMapping("/base/region")
public class TBaseRegionController extends BaseController {
    @Autowired
    private ITBaseRegionService tBaseRegionService;

    @Autowired
    private TBaseRegionMapper tBaseRegionMapper;

    @Autowired
    private ITBasePlantService tBasePlantService;

    /**
     * 查询区域列表
     */
    @PreAuthorize("@ss.hasPermi('base:region:list')")
    @GetMapping("/list")
    public TableDataInfo list(TBaseRegion tBaseRegion) {
        startPage();
        List<TBaseRegion> list = tBaseRegionService.selectTBaseRegionList(tBaseRegion);
        return getDataTable(list);
    }

    @GetMapping("/allRegion/{plantId}")
    public AjaxResult allRegion(@PathVariable("plantId") Long plantId) {
        return AjaxResult.success(tBaseRegionService.selectAllRegionByPlantId(plantId));
    }

    /**
     * 导出区域列表
     */
    @PreAuthorize("@ss.hasPermi('base:region:export')")
    @Log(title = "区域导出", businessType = BusinessType.EXPORT)
    @RepeatSubmit
    @PostMapping("/export")
    public void export(HttpServletResponse response, TBaseRegion tBaseRegion) {
        List<TBaseRegion> list = tBaseRegionService.selectTBaseRegionList(tBaseRegion);
        ExcelUtil<TBaseRegion> util = new ExcelUtil<TBaseRegion>(TBaseRegion.class);
        util.exportExcel(response, list, "区域数据");
    }

    /**
     * 获取区域详细信息
     */
    @PreAuthorize("@ss.hasPermi('base:region:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(tBaseRegionService.selectTBaseRegionById(id));
    }

    /**
     * 新增区域
     */
    @PreAuthorize("@ss.hasPermi('base:region:add')")
    @Log(title = "区域新增", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody TBaseRegion tBaseRegion) {
        TBaseRegion region = tBaseRegionService.selectTBaseRegionByName(tBaseRegion.getRegionName(), tBaseRegion.getPlantId());
        if (region != null) {
            return AjaxResult.error("当前装置下已存在该区域!");
        }
        tBaseRegion.setCreaterCode(getUserId());
        tBaseRegion.setUpdaterCode(getUserId());
        tBaseRegion.setUpdatedate(new Date());
        return toAjax(tBaseRegionService.insertTBaseRegion(tBaseRegion));
    }

    /**
     * 修改区域
     */
    @PreAuthorize("@ss.hasPermi('base:region:edit')")
    @Log(title = "区域修改", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody TBaseRegion tBaseRegion) {
        QueryWrapper<TBaseRegion> wrapper = new QueryWrapper<>();
        wrapper.eq("plant_id", tBaseRegion.getPlantId()).eq("region_name",tBaseRegion.getRegionName()).ne("region_id",tBaseRegion.getRegionId());
        if(CollectionUtils.isNotEmpty(tBaseRegionMapper.selectList(wrapper))){
            return AjaxResult.error("不可修改为已存在的数据");
        }
        tBaseRegion.setUpdaterCode(getUserId());
        tBaseRegion.setUpdatedate(new Date());
        return toAjax(tBaseRegionService.updateTBaseRegion(tBaseRegion));
    }

    @PutMapping("/handleApprove")
    @RepeatSubmit
    @Log(title = "区域审核", businessType = BusinessType.APPROVE)
    public AjaxResult handleApprove(@RequestBody TBaseRegion tBaseRegion) {
        tBaseRegion.setApproveTime(new Date());
        int i = tBaseRegionService.updateTBaseRegionByRegionIds(tBaseRegion);
        if (i >0) {
            return AjaxResult.success();
        }
        return AjaxResult.error("送审/审核失败");
    }

    /**
     * 删除区域
     */
    @PreAuthorize("@ss.hasPermi('base:region:remove')")
    @Log(title = "区域删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tBaseRegionService.deleteTBaseRegionByIds(ids));
    }

    @PreAuthorize("@ss.hasPermi('base:region:remove')")
    @Log(title = "区域作废", businessType = BusinessType.DELETE)
    @DeleteMapping("/handleDisabled/{id}")
    public AjaxResult handleDisabled(@PathVariable Long id) {
        return toAjax(tBaseRegionService.disabledRegion(id));
    }

    @Log(title = "受控区域台账导入", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping("/importData")
    public AjaxResult importData(@RequestParam("file") MultipartFile file) {
        //获取操作人员ID
        Long userId = getUserId();
        //报错行数统计
        List<Integer> failRow = new ArrayList<>();
        Workbook workbook = ExcelUtils.getWorkBook(file);
        Sheet sheet = workbook.getSheetAt(0);
        List<TBaseRegion> list = new ArrayList<>();
        int rowNum = sheet.getPhysicalNumberOfRows();
        int failNumber = 0;
        aa:for (int i = 1; i < rowNum; i++) {
            try {
                logger.info("读取行数：" + i);
                Row row = sheet.getRow(i);
                int cellNum = row.getLastCellNum();
                TBaseRegion entity = new TBaseRegion();
                Long plantId = 0L;
                for (int j = 0; j < cellNum; j++) {
                    Cell cell = row.getCell(j);
                    if (cell == null) {
                        continue;
                    }
                    String cellValue = ExcelUtils.getCellValue(cell);
                    logger.info("cellValue:" + cellValue);
                    if (j == 0) {
                        //装置名称
                        TBasePlant tBasePlant = tBasePlantService.selectTBasePlantByName(cellValue);
                        if (tBasePlant == null|| StringUtils.isEmpty(cellValue)) {
                            failNumber++;
                            logger.info("未找到装置");
                            failRow.add(i + 1);
                            continue aa;
                        }
                        plantId = tBasePlant.getPlantId();
                        entity.setPlantId(plantId);
                    } else if (j == 1) {
                        //  区域名称
                        entity.setRegionCode(cellValue);
                    } else if (j == 2) {
                        // 设备描述
                        entity.setRegionName(cellValue);
                    } else if (j == 3) {
                        //设备编号
                        entity.setRemarks(cellValue);
                    }
                }
                entity.setCreaterCode(userId);
                entity.setApproveStatus(0L);
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
        for (TBaseRegion t : list) {
            failNum++;
            try {
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
