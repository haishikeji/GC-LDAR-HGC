<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="编号" prop="plantCode">
        <el-input
          v-model="queryParams.plantCode"
          placeholder="请输入编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="名称" prop="plantName">
        <el-input
          v-model="queryParams.plantName"
          placeholder="请输入名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="类别" prop="plantType">
        <el-select v-model="queryParams.plantType" @change="handleQuery" placeholder="请选择类别" clearable size="small"
                   style="width: 200px">
          <el-option
            v-for="dict in plantTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="加工/生产能力(t/a)" prop="proAbility" label-width="140px">
        <el-input
          v-model="queryParams.proAbility"
          placeholder="请输入加工/生产能力(t/a)"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['base:plant:add']"
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
          v-hasPermi="['base:plant:edit']"
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
          v-hasPermi="['base:plant:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['base:plant:export']"
        >导出
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-position"
          size="mini"
          :disabled="multiple"
          @click="handleToApprove"
        >送审
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-s-check"
          size="mini"
          :disabled="multiple"
          @click="handleApprove"
          v-hasPermi="['base:plant:approve']"
        >审核
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" ref="plantTable" :data="plantList" @selection-change="handleSelectionChange"
              :height="clientHeight" border :cell-style="tableCellStyle">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="审核状态"  align="center" prop="approveStatus"
                       :formatter="approveStatusFormat"/>
      <el-table-column label="归属机构" align="center" prop="company"/>
      <el-table-column label="编号" align="center" prop="plantCode"/>
      <el-table-column label="名称" align="center" prop="plantName"/>
      <el-table-column label="加工/生产能力(t/a)" align="center" prop="proAbility"/>
      <el-table-column label="类别" align="center" prop="plantType" :formatter="plantTypeFormat"/>
      <el-table-column label="最后维护人" align="center" prop="updater"/>
      <el-table-column label="维护时间" align="center" prop="updatedate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updatedate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="描述" align="center" prop="remarks"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['base:plant:edit']"
            v-if="scope.row.approveStatus==0"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['base:plant:remove']"
            v-if="scope.row.approveStatus==0"
          >删除
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-circle-close"
            @click="handleDisabled(scope.row.plantId)"
            v-hasPermi="['base:plant:remove']"
            v-if="scope.row.approveStatus==2"
          >作废
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

    <!-- 添加或修改装置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="归属机构" prop="company">
          <el-input v-model="form.company" placeholder="请输入归属机构"/>
        </el-form-item>
        <el-form-item label="编号" prop="plantCode">
          <el-input v-model="form.plantCode" placeholder="请输入编号"/>
        </el-form-item>
        <el-form-item label="名称" prop="plantName">
          <el-input v-model="form.plantName" placeholder="请输入名称"/>
        </el-form-item>
        <el-form-item label="类别" prop="plantType">
          <el-select v-model="form.plantType" placeholder="请选择类别" clearable size="small" style="width: 100%">
            <el-option
              v-for="dict in plantTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="加工/生产能力" prop="proAbility">
          <el-input v-model="form.proAbility" placeholder="请输入加工/生产能力"/>
        </el-form-item>
        <el-form-item label="描述" prop="remarks">
          <el-input v-model="form.remarks" placeholder="请输入描述"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listPlant, getPlant, delPlant, addPlant, updatePlant, handleApprove, handleDisabled} from "@/api/base/plant";
import {MessageBox} from "element-ui";

export default {
  name: "Plant",
  data() {
    return {
      plantTypeOptions: [],
      approveStatusOperation: [],
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
      // 装置表格数据
      plantList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        plantCode: null,
        plantName: null,
        proAbility: null,
        plantType: null,
        remarks: null,
        approveStatus: null,
        approveTime: null,
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
        company: [
          {required: true, message: this.$t('归属机构') + this.$t('不能为空'), trigger: "blur"}
        ],
        plantName: [
          {required: true, message: this.$t('装置名称') + this.$t('不能为空'), trigger: "blur"}
        ],
        plantCode: [
          {required: true, message: this.$t('装置编号') + this.$t('不能为空'), trigger: "blur"}
        ],
        plantType: [
          {required: true, message: this.$t('装置类别') + this.$t('不能为空'), trigger: "blur"}
        ],
      }
    };
  },
  watch: {
    tableData(val) {
      this.doLayout();
    }
  },
  created() {

    //设置表格高度对应屏幕高度
    this.$nextTick(() => {
      this.clientHeight = (document.body.clientHeight - 80) * 0.8
    });
    this.getDicts("plant_type").then(response => {
      this.plantTypeOptions = response.data;
    });
    this.getDicts("base_approve_status").then(response => {
      this.approveStatusOperation = response.data;
    });
    this.getList();
  },
  methods: {
    tableCellStyle({row, column, rowIndex, columnIndex}) {
      if (columnIndex === 1 && row.approveStatus == 2) {
        return "color:#13C2C2;";
      }
      if (columnIndex === 1 && row.approveStatus == 1) {
        return "color:#0000FF;";
      }
      if (columnIndex === 1 && row.approveStatus == 0) {
        return "color:#ff0000;";
      }
    },
    /* 重新渲染table组件 */
    doLayout(){
      this.$nextTick(() => {
        this.$refs.plantTable.doLayout()
      })
    },
    plantTypeFormat(row, column) {
      return this.selectDictLabel(this.plantTypeOptions, row.plantType);
    },
    approveStatusFormat(row, column) {
      return this.selectDictLabel(this.approveStatusOperation, row.approveStatus);
    },
    /** 查询装置列表 */
    getList() {
      this.loading = true;
      listPlant(this.queryParams).then(response => {
        this.plantList = response.rows;
        this.total = response.total;
        this.$nextTick(() => {
          this.$refs.plantTable.doLayout(); // 解决表格错位
        });
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
        plantId: null,
        company: null,
        plantCode: null,
        plantName: null,
        proAbility: null,
        plantType: null,
        remarks: null,
        approveStatus: 0,
        approveTime: null,
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
      this.ids = selection.map(item => item.plantId)
      this.status = selection.map(item => item.approveStatus)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加装置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const plantId = row.plantId || this.ids
      getPlant(plantId).then(response => {
        this.form = response.data;
        if (this.form.approveStatus!=0){
          MessageBox.alert('已送审/已审核的数据不可修改！', '注意！', {
            confirmButtonText: '确定',
          })
          return;
        }
        this.open = true;
        this.title = "修改装置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.plantId != null) {
            updatePlant(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPlant(this.form).then(response => {
              this.$modal.msgSuccess(response.msg);
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleDisabled(id){
      this.$modal.confirm('是否确认作废数据项？注意！该操作不可逆！作废后该装置下的所有区域、设备、密封点都将一并作废！').then(function () {
        return handleDisabled(id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("作废成功");
      }).catch(() => {
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.plantId || this.ids;
      if(row.approveStatus===undefined){
        for (let i = 0; i < this.status.length; i++) {
          if (this.status[i] != 0) {
            MessageBox.alert('已送审/已审核的数据不可删除！', '注意！', {
              confirmButtonText: '确定',
            })
            return
          }
        }
      }else if (row.approveStatus!=0){
        MessageBox.alert('已送审/已审核的数据不可删除！', '注意！', {
          confirmButtonText: '确定',
        })
        return
      }
      this.$modal.confirm('是否确认删除数据项？').then(function () {
        return delPlant(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('base/plant/export', {
        ...this.queryParams
      }, `装置台账_${new Date().getTime()}.xlsx`)
    },
    handleToApprove() {
      let data={};
      data.plantIds=this.ids;
      data.approveStatus=1;
      handleApprove(data).then(()=>{
        this.getList();
      })
    },handleApprove() {
      let data={};
      data.plantIds=this.ids;
      data.approveStatus=2;
      handleApprove(data).then(()=>{
        this.getList();
      })
    }
  }
};
</script>
