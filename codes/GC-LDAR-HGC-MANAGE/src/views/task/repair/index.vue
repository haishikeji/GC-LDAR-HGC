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
        <el-select v-model="queryParams.taskType" @change="handleQuery" placeholder="请选择任务类型" clearable
                   size="small" style="width: 100%">
          <el-option
            v-for="dict in taskTypeOperation"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="任务起始时间" prop="startTime" label-width="100px">
        <el-date-picker clearable @change="handleQuery"
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
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['task:repair:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['task:repair:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['task:repair:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-check"
          size="mini"
          :disabled="multiple"
          @click="handleDivide"
          v-hasPermi="['task:repair:edit']"
        >确认分配
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="repairList" @selection-change="handleSelectionChange" :height="clientHeight"
              :cell-style="tableCellStyle" border>
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="状态" align="center" prop="status" fixed="left" width="85" :formatter="divideFormat"/>
      <el-table-column label="是否超时" align="center" prop="timeOut" fixed="left" width="85"/>
      <el-table-column label="装置名称" align="center" prop="plantName" :show-overflow-tooltip="true" width="130"/>
      <el-table-column label="任务名称" align="center" prop="taskName" :show-overflow-tooltip="true" width="130"/>
      <el-table-column label="任务编号" align="center" prop="taskCode" :show-overflow-tooltip="true" width="130"/>
      <el-table-column label="任务类型" align="center" prop="taskType" :show-overflow-tooltip="true" width="130"
                       :formatter="taskTypeFormat"/>
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
      <el-table-column label="接收人" align="center" prop="recipientName" :show-overflow-tooltip="true" width="130"/>
      <el-table-column label="维修点数" align="center" prop="taskNum" :show-overflow-tooltip="true" width="130"/>
      <el-table-column label="已维修点数" align="center" prop="taskDoneNum" :show-overflow-tooltip="true" width="130"/>
      <el-table-column label="未维修点数" align="center" prop="taskUndoneNum" :show-overflow-tooltip="true"
                       width="130"/>
      <el-table-column label="完成率" align="center" width="250" prop="percentage">
        <template slot-scope="scope">
          <el-progress :text-inside="true" :stroke-width="30" :percentage="scope.row.percentage"
                       status="success"></el-progress>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remarks" :show-overflow-tooltip="true" width="130"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="130">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-order"
            @click="openCheckPoint(scope.row)"
          >维修点清单
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['task:repair:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['task:repair:remove']"
          >删除
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

    <!-- 添加或修改维修任务对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="任务名称" prop="taskName">
          <el-input v-model="form.taskName" placeholder="请输入任务名称"/>
        </el-form-item>
        <el-form-item label="任务编号" prop="taskCode">
          <el-input v-model="form.taskCode" placeholder="请输入任务编号"/>
        </el-form-item>
        <el-form-item label="装置" prop="plantId">
          <el-select v-model="form.plantId" placeholder="请选择装置" clearable size="small" style="width: 100%">
            <el-option
              v-for="dict in plantOperation"
              :key="dict.plantId"
              :label="dict.plantName"
              :value="dict.plantId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="任务类型" prop="taskType">
          <el-select v-model="form.taskType" placeholder="请选择任务类型" clearable size="small" style="width: 100%">
            <el-option
              v-for="dict in taskTypeOperation"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="任务起始时间" prop="startTime">
          <el-date-picker clearable style="width: 100%"
                          v-model="form.startTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择任务起始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="任务截止时间" prop="endTime">
          <el-date-picker clearable style="width: 100%"
                          v-model="form.endTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择任务截止时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="接收人" prop="recipient">
          <el-select v-model="form.recipient" placeholder="请选择接收人" clearable size="small" style="width: 100%">
            <el-option
              v-for="dict in userOperation"
              :key="dict.userId"
              :label="dict.userName"
              :value="dict.userId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="form.remarks" placeholder="请输入备注"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <repair-point v-if="pointVisible" ref="repairPointList"></repair-point>
    <repair-enter v-if="enterVisible" ref="repairEnterList"></repair-enter>
  </div>
</template>

<script>
import {listRepair, getRepair, delRepair, addRepair, updateRepair, divideRepair} from "@/api/task/repair";
import {divideInspection} from "@/api/task/inspection";
import {getAllPlantName} from "@/api/base/plant";
import RepairPoint from "@/views/task/repair/repairPoint";
import RepairEnter from "@/views/check/repairCheck/repairEnter";
import {MessageBox} from "element-ui";
import {allUser} from "@/api/system/user";

export default {
  name: "Repair",
  components: {RepairEnter, RepairPoint},
  data() {
    return {
      pointVisible: false,
      enterVisible: false,
      divideOperation: [],
      userOperation: [],
      taskTypeOperation: [],
      plantOperation: [],
      // 页面高度
      clientHeight: 300,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      divideStatus: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: false,
      // 总条数
      total: 0,
      // 维修任务表格数据
      repairList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        plantId: null,
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
      rules: {
        plantId: [
          {required: true, message: '请选择装置', trigger: "blur"}
        ],
        taskType: [
          {required: true, message: '请选择任务类型', trigger: "blur"}
        ],
        taskCode: [
          {required: true, message: '请输入任务编号', trigger: "blur"}
        ],
        taskName: [
          {required: true, message: '请输入任务名称', trigger: "blur"}
        ],
        startTime: [
          {required: true, message: '请选择任务开始时间', trigger: "blur"}
        ],
        endTime: [
          {required: true, message: '请选择任务结束时间', trigger: "blur"}
        ],
        recipient: [
          {required: true, message: '请选择接收人', trigger: "blur"}
        ],
      }
    };
  },
  created() {
    this.getList();
    //设置表格高度对应屏幕高度
    this.$nextTick(() => {
      this.clientHeight = (document.body.clientHeight - 80) * 0.8
    });
    this.getDicts("divide_status").then(response => {
      this.divideOperation = response.data;
    });
    this.getDicts("repair_type").then(response => {
      this.taskTypeOperation = response.data;
    });
    getAllPlantName().then(response => {
      this.plantOperation = response.data;
    });
    allUser({}).then(res=>{
      this.userOperation=res.data;
    })
  },
  methods: {
    openCheckPoint(row) {
      if (row.status == 1) {
        row.isEnter = false;
        this.enterVisible = true;
        this.$nextTick(() => {
          this.$refs.repairEnterList.openDialog(row)
        })
      } else {
        this.pointVisible = true;
        this.$nextTick(() => {
          this.$refs.repairPointList.openDialog(row)
        })
      }
    },
    taskTypeFormat(row, column) {
      return this.selectDictLabel(this.taskTypeOperation, row.taskType);
    },
    tableCellStyle({row, column, rowIndex, columnIndex}) {
      if (columnIndex === 1 && row.status == 0) {
        return "color:#ff0000;";
      }
      if (columnIndex === 1 && row.status == 1) {
        return "color:#00cc00;";
      }
      if (columnIndex === 2 && row.timeOut === '是') {
        return "color:#ff0000;";
      }
      if (columnIndex === 2 && row.timeOut === '否') {
        return "color:#00cc00;";
      }
      if (columnIndex === 6) {
        return "color:#00d;";
      }
    },
    handleDivide() {
      console.log(this.divideStatus)
      for (let statusKey of this.divideStatus) {
        if (statusKey === "1") {
          this.$alert('已分配的任务不可再次分配！', '注意！', {
            confirmButtonText: '确定',
          });
          return
        }
      }
      this.reset();
      const ids = this.ids
      this.$modal.confirm('是否确认分配？').then(function () {
        return divideRepair(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("分配成功");
      }).catch(() => {
        this.$modal.msg("取消分配");
      });
    },
    divideFormat(row, column) {
      return this.selectDictLabel(this.divideOperation, row.status);
    },
    /** 查询维修任务列表 */
    getList() {
      this.loading = true;
      listRepair(this.queryParams).then(response => {
        this.repairList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        plantId: null,
        taskName: null,
        taskCode: null,
        taskType: '1',
        startTime: null,
        endTime: null,
        recipient: null,
        taskNum: null,
        taskDoneNum: null,
        taskUndoneNum: null,
        status: "0",
        remarks: null,
        deptId: null,
        delFlag: null,
        createrCode: null,
        createdate: null,
        updaterCode: null,
        updatedate: null
      };
      this.resetForm("form");
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
      this.divideStatus = selection.map(item => item.status)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加维修任务";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getRepair(id).then(response => {
        this.form = response.data;
        if (this.form.status != 0) {
          MessageBox.alert('已分配的任务不可修改！', '注意！', {
            confirmButtonText: '确定',
          })
          return
        }
        this.open = true;
        this.title = "修改维修任务";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateRepair(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRepair(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      if (row.status === undefined) {
        for (let i = 0; i < this.divideStatus.length; i++) {
          if (this.divideStatus[i] != 0) {
            MessageBox.alert('已分配的任务不可删除！', '注意！', {
              confirmButtonText: '确定',
            })
            return
          }
        }
      } else if (row.status != 0) {
        MessageBox.alert('已分配的任务不可删除！', '注意！', {
          confirmButtonText: '确定',
        })
        return
      }
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除数据项？').then(function () {
        return delRepair(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('task/repair/export', {
        ...this.queryParams
      }, `维修任务清单_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
