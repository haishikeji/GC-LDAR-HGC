<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="年份" prop="year">
        <el-date-picker
          v-model="queryParams.year"
          type="year"
          clearable
          @change="handleQuery"
          placeholder="选择年份">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
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
          v-hasPermi="['task:plan:add']"
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
          v-hasPermi="['task:plan:edit']"
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
          v-hasPermi="['task:plan:remove']"
        >删除
        </el-button>
      </el-col>
      <!--      <el-col :span="1.5">
              <el-button
                type="info"
                plain
                icon="el-icon-refresh"
                size="mini"
                @click="handleDoPlan"
              >{{ $t('校验检测计划') }}
              </el-button>
            </el-col>-->
      <el-col :span="1.5">
        <span style="color: #ff0000" v-if="checking"><i class="el-icon-loading"></i>正在校验检测计划，请稍候...</span>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="planList" @selection-change="handleSelectionChange" :height="clientHeight"
              border>
      <el-table-column type="selection" width="55" align="center" fixed="left"/>
      <el-table-column label="装置名称" align="center" prop="plantName" width="130" :show-overflow-tooltip="true"/>
      <el-table-column label="检测计划编号" align="center" prop="inspectionPlanNo" width="200"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="检测计划名称" align="center" prop="inspectionPlanName" width="300"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="检测频次" align="center" prop="detectionFrequency" width="130"
                       :show-overflow-tooltip="true" :formatter="detectionFormat"/>
      <el-table-column label="检测起始时间" align="center" prop="startTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="检测截止时间" align="center" prop="endTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="检测点数" align="center" prop="pointNum" width="120" :show-overflow-tooltip="true"/>
      <el-table-column label="创建人" align="center" prop="creater" width="120" :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createdate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remarks" :show-overflow-tooltip="true" width="180"/>
      <el-table-column label="操作" fixed="right" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-order"
            @click="openPlanPoint(scope.row)"
          >计划检测点清单
          </el-button>
          <br>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['task:plan:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['task:plan:remove']"
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

    <!-- 添加或修改检测计划对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="装置" prop="plantId">
          <el-select v-model="form.plantId" :disabled="edit" placeholder="请选择装置" clearable size="small"
                     style="width: 100%">
            <el-option
              v-for="dict in plantOperation"
              :key="dict.plantId"
              :label="dict.plantName"
              :value="dict.plantId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="检测计划编号" prop="inspectionPlanNo">
          <el-input v-model="form.inspectionPlanNo" placeholder="请输入检测计划编号"/>
        </el-form-item>
        <el-form-item label="检测计划名称" prop="inspectionPlanName">
          <el-input v-model="form.inspectionPlanName" placeholder="请输入检测计划名称"/>
        </el-form-item>
        <el-form-item label="检测频次" prop="detectionFrequency">
          <el-select v-model="form.detectionFrequency" :disabled="edit" @change="getEndTime"
                     placeholder="请选择检测频次" clearable size="small" style="width: 100%">
            <el-option
              v-for="dict in detectionOperation"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="检测起始时间" prop="startTime">
          <el-date-picker clearable style="width: 100%"
                          v-model="form.startTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          @change="getEndTime"
                          placeholder="请选择检测起始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="检测截止时间" prop="endTime">
          <el-date-picker clearable style="width: 100%"
                          v-model="form.endTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择检测截止时间">
          </el-date-picker>
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
    <plan-point v-if="pointVisible" ref="planPointList"></plan-point>
    <point-list v-if="pointListVisible" ref="pointList"></point-list>
  </div>
</template>

<script>
import {listPlan, getPlan, delPlan, addPlan, updatePlan, checkPlan} from "@/api/task/plan";
import dayjs from "dayjs";
import {getAllPlantName} from "@/api/base/plant";
import PlanPoint from "@/views/task/plan/planPoint";
import PointList from "@/views/task/plan/pointList";

export default {
  name: "Plan",
  components: {PointList, PlanPoint},
  data() {
    return {
      checking: false,
      plantOperation: [],
      detectionOperation: [],
      // 页面高度
      clientHeight: 300,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      pointVisible: false,
      pointListVisible: false,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: false,
      // 总条数
      total: 0,
      // 检测计划表格数据
      planList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      edit: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        plantId: null,
        haveSub: null,
        inspectionPlanNo: null,
        inspectionPlanName: null,
        detectionFrequency: null,
        startTime: null,
        endTime: null,
        pointNum: null,
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
        inspectionPlanNo: [
          {required: true, message: '请输入检测计划编号', trigger: "blur"}
        ],
        inspectionPlanName: [
          {required: true, message: '请输入检测计划名称', trigger: "blur"}
        ],
        detectionFrequency: [
          {required: true, message: '请选择检测频次', trigger: "blur"}
        ],
        startTime: [
          {required: true, message: '请选择检测起始时间', trigger: "blur"}
        ],
        endTime: [
          {required: true, message: '请选择检测截止时间', trigger: "blur"}
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
    getAllPlantName().then(response => {
      this.plantOperation = response.data;
    });
    this.getDicts("detection_frequency").then(response => {
      this.detectionOperation = response.data;
    });
  },
  methods: {
    openPlanPoint(row) {
      if (row.haveSub == 1 || row.detectionFrequency != 5) {
        this.pointListVisible = true;
        this.$nextTick(() => {
          this.$refs.pointList.init(row)
        })
      } else {
        this.pointVisible = true;
        this.$nextTick(() => {
          this.$refs.planPointList.openDialog(row)
        })
      }
    },
    getEndTime() {
      if (this.form.startTime && this.form.detectionFrequency) {
        if (this.form.detectionFrequency == 1) {
          this.form.endTime = dayjs(new Date(this.form.startTime)).add(1, "month").subtract(1, 'day').format("YYYY-MM-DD")
        } else if (this.form.detectionFrequency == 2) {
          this.form.endTime = dayjs(new Date(this.form.startTime)).add(3, "month").subtract(1, 'day').format("YYYY-MM-DD")
        } else if (this.form.detectionFrequency == 3) {
          this.form.endTime = dayjs(new Date(this.form.startTime)).add(6, "month").subtract(1, 'day').format("YYYY-MM-DD")
        } else if (this.form.detectionFrequency == 4) {
          this.form.endTime = dayjs(new Date(this.form.startTime)).add(1, "year").subtract(1, 'day').format("YYYY-MM-DD")
        }
      }
    },
    detectionFormat(row, column) {
      return this.selectDictLabel(this.detectionOperation, row.detectionFrequency);
    },
    /** 查询检测计划列表 */
    getList() {
      this.loading = true;
      listPlan(this.queryParams).then(response => {
        this.planList = response.rows;
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
        inspectionPlanNo: null,
        inspectionPlanName: null,
        detectionFrequency: null,
        startTime: null,
        endTime: null,
        pointNum: null,
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
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.edit = false;
      this.title = "添加检测计划";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.edit = true;
      const id = row.id || this.ids
      getPlan(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改检测计划";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePlan(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPlan(this.form).then(response => {
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
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除数据项？已创建检测任务的计划将不会删除！').then(function () {
        return delPlan(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('task/plan/export', {
        ...this.queryParams
      }, `检测计划_${new Date().getTime()}.xlsx`)
    },
    /** 校验按钮操作 */
    handleDoPlan() {
      this.$modal.confirm('是否确认校验检测计划？注意！在此之后新增的检测点将记入下次检测计划！').then(() => {
        this.checking = true;
        checkPlan().then(response => {
          this.checking = false;
          this.getList();
          this.$modal.msgSuccess("校验成功");
        })
      }).catch(() => {
        this.$modal.msg("校验取消");
      })
    }
  }
};
</script>
