<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="仪器类型编号" prop="code">
        <el-input
          v-model="queryParams.instrumentCode"
          placeholder="请输入仪器类型编号"
          clearable
          @change="handleQuery"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="仪器类型名称" prop="name">
        <el-input
          v-model="queryParams.instrumentName"
          placeholder="请输入仪器类型名称"
          clearable
          @change="handleQuery"
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
          v-hasPermi="['check:calibration:add']"
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
          v-hasPermi="['check:calibration:edit']"
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
          v-hasPermi="['check:calibration:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['base:device:add']"
        >导入
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['check:calibration:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="calibrationList" @selection-change="handleSelectionChange"
              :height="clientHeight" border>
      <el-table-column type="selection" width="55" fixed="left" align="center"/>
      <el-table-column label="校准时间" align="center" prop="calibrationTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.calibrationTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="校准人员" align="center" prop="calibrationUser" width="130"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="仪器编号" align="center" prop="instrumentCode" width="130" :show-overflow-tooltip="true"/>
      <el-table-column label="仪器名称" align="center" prop="instrumentName" width="130" :show-overflow-tooltip="true"/>
      <el-table-column label="仪器类型" align="center" prop="instrumentType" width="130" :show-overflow-tooltip="true"/>
      <el-table-column label="标准气名称" align="center" prop="standardgasesName" width="130"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="标准气有效期" align="center" prop="standardgasesLifespan" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.standardgasesLifespan, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="标准气浓度(ppm)" align="center" prop="standardgasesConcentration" width="130"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="零气浓度值(ppm)" align="center" prop="zerogasConcentration" width="130"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="零气校准值(ppm)" align="center" prop="zerogasCalibration" width="130"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="标气校准值(ppm)" align="center" prop="standardgasesCalibration" width="130"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="漂移核查值(ppm)" align="center" prop="driftCheck" width="130"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="过滤片检查" align="center" prop="filterCheck" width="130" :show-overflow-tooltip="true" :formatter="filterFormat"/>
      <el-table-column label="气密性检查" align="center" prop="airtightnessCheck" width="130"
                       :show-overflow-tooltip="true" :formatter="airFormat"/>
      <el-table-column label="流量检查(0.1-3)L/min" align="center" prop="flowCheck" width="180"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="气体钢瓶压力(Bar)" align="center" prop="pressure" width="130"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="备注" align="center" prop="remarks" width="130" :show-overflow-tooltip="true"/>
      <el-table-column label="操作" align="center" fixed="right" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['check:calibration:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['check:calibration:remove']"
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

    <!-- 添加或修改检测仪器校准对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-form-item label="校准时间" prop="calibrationTime">
          <el-date-picker clearable
                          v-model="form.calibrationTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择校准时间"
                          style="width: 100%">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="校准人员" prop="calibrationUser">
          <el-input v-model="form.calibrationUser" placeholder="请输入校准人员"/>
        </el-form-item>
        <el-form-item label="仪器" prop="instrumentId">
          <el-select v-model="form.instrumentId" placeholder="请选择仪器" clearable size="small" style="width: 100%">
            <el-option
              v-for="dict in instrumentList"
              :key="dict.id"
              :label="dict.name"
              :value="dict.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="标准气体" prop="standardgasesId">
          <el-select v-model="form.standardgasesId" placeholder="请选择标气" clearable size="small" style="width: 100%">
            <el-option
              v-for="dict in standardgasesList"
              :key="dict.id"
              :label="dict.describe"
              :value="dict.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="零气浓度值(ppm)" prop="zerogasConcentration">
          <el-input v-model="form.zerogasConcentration" placeholder="请输入零气浓度值"/>
        </el-form-item>
        <el-form-item label="零气校准值(ppm)" prop="zerogasCalibration">
          <el-input v-model="form.zerogasCalibration" placeholder="请输入零气校准值"/>
        </el-form-item>
        <el-form-item label="标气校准值(ppm)" prop="standardgasesCalibration">
          <el-input v-model="form.standardgasesCalibration" placeholder="请输入标气校准值"/>
        </el-form-item>
        <el-form-item label="漂移核查值(ppm)" prop="driftCheck">
          <el-input v-model="form.driftCheck" placeholder="请输入漂移核查值"/>
        </el-form-item>
        <el-form-item label="过滤片检查" prop="filterCheck">
          <el-select v-model="form.filterCheck" placeholder="请选择是/否" clearable size="small" style="width: 100%">
            <el-option
              v-for="dict in yesOrNoOperation"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="气密性检查" prop="airtightnessCheck">
          <el-select v-model="form.airtightnessCheck" placeholder="请选择气密性状态" clearable size="small" style="width: 100%">
            <el-option
              v-for="dict in checkOperation"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="流量检查(0.1-3)L/min" prop="flowCheck">
          <el-input v-model="form.flowCheck" placeholder="请输入流量检查"/>
        </el-form-item>
        <el-form-item label="气体钢瓶压力(Bar)" prop="pressure">
          <el-input v-model="form.pressure" placeholder="请输入气体钢瓶压力"/>
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


    <!-- 用户导入对话框 -->
    <el-dialog v-dialogDrag :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">
          {{ $t('将文件拖到此处，或') }}
          <em>{{ $t('点击上传') }}</em>
        </div>
        <div class="el-upload__tip" slot="tip">
          <!--<el-checkbox v-model="upload.updateSupport" />是否更新已经存在的用户数据-->
          <el-link type="info" style="font-size:12px" @click="importTemplate">{{ $t('下载模板') }}</el-link>
        </div>
        <form ref="downloadFileForm" :action="upload.downloadAction" target="FORMSUBMIT">
          <input name="type" :value="upload.type" hidden/>
        </form>
        <div class="el-upload__tip" style="color:red" slot="tip">{{ $t('提示：仅允许导入“xls”或“xlsx”格式文件！') }}</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm" v-loading.fullscreen.lock="fullscreenLoading">{{
            $t('确 定')
          }}
        </el-button>
        <el-button @click="upload.open = false">{{ $t('取 消') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listCalibration,
  getCalibration,
  delCalibration,
  addCalibration,
  updateCalibration
} from "@/api/check/calibration";
import {getAllInstrument} from "@/api/check/instrument";
import {getAllStandardgases} from "@/api/check/standardgases";
import {getToken} from "@/utils/auth";

export default {
  name: "Calibration",
  data() {
    return {
      upload: {
        downloadAction: process.env.VUE_APP_BASE_API + '/common/template',
        //下载模板类型
        type: "calibration",
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: {Authorization: "Bearer " + getToken()},
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/check/calibration/importData"
      },
      instrumentList: [],
      standardgasesList: [],
      yesOrNoOperation: [],
      checkOperation: [],
      // 页面高度
      clientHeight: 300,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: false,
      // 总条数
      total: 0,
      // 检测仪器校准表格数据
      calibrationList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        calibrationTime: null,
        calibrationUser: null,
        instrumentId: null,
        standardgasesId: null,
        zerogasConcentration: null,
        zerogasCalibration: null,
        standardgasesCalibration: null,
        driftCheck: null,
        filterCheck: null,
        airtightnessCheck: null,
        flowCheck: null,
        pressure: null,
        remarks: null,
        deptId: null,
        createrCode: null,
        createdate: null,
        updaterCode: null,
        updatedate: null,
        instrumentCode:null,
        instrumentName:null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        calibrationTime: [
          {required: true, message: '校准时间不能为空', trigger: "blur"}
        ],
        filterCheck: [
          {required: true, message: '过滤片检查不能为空', trigger: "blur"}
        ],
        instrumentId: [
          {required: true, message: '请选择仪器', trigger: "blur"}
        ],
        standardgasesId: [
          {required: true, message: '请选择标气', trigger: "blur"}
        ],
        airtightnessCheck: [
          {required: true, message: '气密性检查不能为空', trigger: "blur"}
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
    getAllInstrument().then(response => {
      this.instrumentList = response.data;
    });
    getAllStandardgases().then(response => {
      this.standardgasesList = response.data;
    });
    this.getDicts("yes_no").then(response => {
      this.yesOrNoOperation = response.data;
    });
    this.getDicts("airtightness_check").then(response => {
      this.checkOperation = response.data;
    });
  },
  methods: {
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
      this.fullscreenLoading = true;
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = this.$t('用户导入');
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      this.$refs['downloadFileForm'].submit()
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.fullscreenLoading = false;
      console.log(response)
      if (response.data === 0) {
        this.$alert(this.$t('导入失败！') + response.msg, this.$t('导入结果'), {dangerouslyUseHTMLString: true});
      } else if (response.data[0] != null) {
        this.$alert(this.$t('成功导入') + response.msg + this.$t('条数据') + "，" + this.$t('第') + response.data + this.$t('行数据出现错误导入失败') + "。", this.$t('导入结果'), {dangerouslyUseHTMLString: true});
      } else {
        this.$alert(this.$t('成功导入') + response.msg + this.$t('条数据'), this.$t('导入结果'), {dangerouslyUseHTMLString: true});
      }
      this.getList();
    },
    filterFormat(row, column) {
      return this.selectDictLabel(this.yesOrNoOperation, row.filterCheck);
    },
    airFormat(row, column) {
      return this.selectDictLabel(this.checkOperation, row.airtightnessCheck);
    },
    /** 查询检测仪器校准列表 */
    getList() {
      this.loading = true;
      listCalibration(this.queryParams).then(response => {
        this.calibrationList = response.rows;
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
        calibrationTime: null,
        calibrationUser: null,
        instrumentId: null,
        standardgasesId: null,
        zerogasConcentration: null,
        zerogasCalibration: null,
        standardgasesCalibration: null,
        driftCheck: null,
        filterCheck: null,
        airtightnessCheck: null,
        flowCheck: null,
        pressure: null,
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
      this.title = "添加检测仪器校准";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCalibration(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改检测仪器校准";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCalibration(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCalibration(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除检测仪器校准编号为"' + ids + '"的数据项？').then(function () {
        return delCalibration(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('check/calibration/export', {
        ...this.queryParams
      }, `检测仪器校准清单_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
