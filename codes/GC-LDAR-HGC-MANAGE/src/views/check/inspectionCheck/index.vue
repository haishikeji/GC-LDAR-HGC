<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="任务名称" prop="taskName">
        <el-input
          v-model="queryParams.taskName"
          placeholder="请输入任务名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="任务类型" prop="planId">
        <el-select v-model="form.taskType" placeholder="请选择任务类型" @change="handleQuery" clearable size="small" style="width: 100%">
          <el-option
            v-for="dict in taskTypeOperation"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="任务起始时间" prop="startTime" label-width="100px">
        <el-date-picker clearable  @change="handleQuery"
                        v-model="queryParams.startTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择任务起始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="任务截止时间" prop="endTime" label-width="100px">
        <el-date-picker clearable @change="handleQuery"
                        v-model="queryParams.endTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择任务截止时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="inspectionList" @selection-change="handleSelectionChange"
              :height="clientHeight" border :cell-style="tableCellStyle">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="是否超时" align="center" prop="timeOut" fixed="left" width="85"/>
      <el-table-column label="装置名称" align="center" prop="plantName" :show-overflow-tooltip="true" width="130"/>
      <el-table-column label="计划名称" align="center" prop="planName" :show-overflow-tooltip="true" width="280"/>
      <el-table-column label="任务名称" align="center" prop="taskName" :show-overflow-tooltip="true" width="130"/>
      <el-table-column label="任务编号" align="center" prop="taskCode" :show-overflow-tooltip="true" width="130"/>
      <el-table-column label="任务类型" align="center" prop="taskType" :show-overflow-tooltip="true" :formatter="taskTypeFormat"
       width="130"/>
      <el-table-column label="任务起始时间" align="center" prop="startTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="任务截止时间" align="center" prop="endTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="接收人" align="center" prop="recipient" :show-overflow-tooltip="true" width="130"/>
      <el-table-column label="任务点数" align="center" prop="taskNum" :show-overflow-tooltip="true" width="100"/>
      <el-table-column label="已检测点数" align="center" prop="taskDoneNum" :show-overflow-tooltip="true" width="100"/>
      <el-table-column label="未检测点数" align="center" prop="taskUndoneNum" :show-overflow-tooltip="true"
                       width="100"/>
      <el-table-column label="完成率" align="center" width="250" prop="percentage">
        <template slot-scope="scope">
          <el-progress :text-inside="true" :stroke-width="30" :percentage="scope.row.percentage"
                       status="success"></el-progress>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remarks" :show-overflow-tooltip="true" width="130"/>
      <el-table-column label="操作" align="center" fixed="right" width="120" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-order"
            @click="openPointVisible(scope.row)"
          >检测点清单
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <point-enter v-if="pointVisible" ref="pointList"></point-enter>
  </div>
</template>

<script>
import {
  addInspection,
  delInspection,
  divideInspection,
  getInspection,
  listInspection,
  updateInspection
} from "@/api/task/inspection";
import {getAllPlan} from "@/api/task/plan";
import PointEnter from "@/views/check/inspectionCheck/pointEnter";

export default {
  name: "InspectionCheck",
  components: {PointEnter},
  data() {
    return {
      pointVisible:false,
      taskTypeOperation: [],
      planOperation: [],
      divideOperation: [],
      timeRange:[],
      // 页面高度
      clientHeight: 300,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      status: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: false,
      // 总条数
      total: 0,
      // 检测任务表格数据
      inspectionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        plantId: null,
        planId: null,
        taskName: null,
        taskCode: null,
        taskType: null,
        startTime: null,
        endTime: null,
        recipient: null,
        taskNum: null,
        taskDoneNum: null,
        taskUndoneNum: null,
        status: null,
        remarks: null,
        deptId: null,
        createrCode: null,
        createdate: null,
        updaterCode: null,
        updatedate: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {}
    };
  },
  created() {
    this.getList();
    //设置表格高度对应屏幕高度
    this.$nextTick(() => {
      this.clientHeight = (document.body.clientHeight - 80) * 0.8
    });
    this.getDicts("task_type").then(response => {
      this.taskTypeOperation = response.data;
    });
    this.getDicts("divide_status").then(response => {
      this.divideOperation = response.data;
    });
    getAllPlan().then(response => {
      this.planOperation = response.data;
    });
  },
  methods: {
    openPointVisible(row){
      row.isEnter=true;
      this.pointVisible = true;
      this.$nextTick(() => {
        this.$refs.pointList.openDialog(row)
      })
    },
    tableCellStyle({row, column, rowIndex, columnIndex}) {
      if (columnIndex === 1 && row.timeOut === '是') {
        return "color:#ff0000;";
      }
      if (columnIndex === 1 && row.timeOut === '否') {
        return "color:#00cc00;";
      }
      if (columnIndex === 6 && row.taskType == 2) {
        return "color:#ff0000;";
      }
      if (columnIndex === 6 && row.taskType == 1) {
        return "color:#00cc00;";
      }
    },
    taskTypeFormat(row, column) {
      return this.selectDictLabel(this.taskTypeOperation, row.taskType);
    },
    divideFormat(row, column) {
      return this.selectDictLabel(this.divideOperation, row.status);
    },
    /** 查询检测任务列表 */
    getList() {
      this.loading = true;
      listInspection(this.queryParams).then(response => {
        this.inspectionList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('task/inspection/export', {
        ...this.queryParams
      }, `检测任务清单_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
