<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="装置" prop="plantId">
        <el-select v-model="queryParams.plantId" @change="handleQuery" placeholder="请选择装置" clearable size="small" style="width: 85%">
          <el-option
            v-for="dict in plantOperation"
            :key="dict.plantId"
            :label="dict.plantName"
            :value="dict.plantId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="仪器id" prop="instrumentId">
        <el-input
          v-model="queryParams.instrumentId"
          placeholder="请输入仪器id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="检测人员" prop="tester">
        <el-input
          v-model="queryParams.tester"
          placeholder="请输入检测人员"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="检测日期" prop="testTime">
        <el-date-picker
          v-model="chooseTime"
          type="daterange"
          align="right"
          unlink-panels
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :picker-options="pickerOptions">
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
          v-hasPermi="['check:environment:add']"
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
          v-hasPermi="['check:environment:edit']"
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
          v-hasPermi="['check:environment:remove']"
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
          v-hasPermi="['check:environment:add']"
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
          v-hasPermi="['check:environment:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="environmentList" @selection-change="handleSelectionChange"
              :height="clientHeight" border>
      <el-table-column type="selection" width="55" align="center" fixed="left"/>
      <el-table-column label="装置编码" align="center" prop="plantCode" width="100" :show-overflow-tooltip="true"/>
      <el-table-column label="装置名称" align="center" prop="plantName" width="120" :show-overflow-tooltip="true"/>
      <el-table-column label="仪器型号" align="center" prop="instrumentModel" width="100"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="仪器编号" align="center" prop="instrumentCode" width="120" :show-overflow-tooltip="true"/>
      <el-table-column label="组件编码" align="center" prop="assemblyCode" width="130" :show-overflow-tooltip="true"/>
      <el-table-column label="环境本底值(ppm)" align="center" prop="backgroundValue" width="130"
                       :show-overflow-tooltip="true"/>
      <el-table-column label="环境温度(℃)" align="center" prop="ambientTemp" width="130" :show-overflow-tooltip="true"/>
      <el-table-column label="环境湿度" align="center" prop="ambientHumidity" width="90" :show-overflow-tooltip="true"/>
      <el-table-column label="大气压(kpa)" align="center" prop="atmos" width="130" :show-overflow-tooltip="true"/>
      <el-table-column label="风向" align="center" prop="windDirection" width="90" :show-overflow-tooltip="true"/>
      <el-table-column label="风速(m/s)" align="center" prop="windSpeed" width="90" :show-overflow-tooltip="true"/>
      <el-table-column label="检测人员" align="center" prop="tester" width="130" :show-overflow-tooltip="true"/>
      <el-table-column label="检测日期" align="center" prop="testTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.testTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="最后维护人" align="center" prop="updater" width="130" :show-overflow-tooltip="true"/>
      <el-table-column label="维护时间" align="center" prop="updatedate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updatedate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remarks" width="180" :show-overflow-tooltip="true"/>

      <el-table-column label="操作" align="center" fixed="right" width="130" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['check:environment:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['check:environment:remove']"
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

    <!-- 添加或修改环境本底值记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="75%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="装置" prop="plantId">
              <el-select v-model="form.plantId" placeholder="请选择装置" clearable size="small" style="width: 85%">
                <el-option
                  v-for="dict in plantOperation"
                  :key="dict.plantId"
                  :label="dict.plantName"
                  :value="dict.plantId"
                />
              </el-select>
              &nbsp;<el-checkbox v-model="checked">特殊</el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="仪器" prop="instrumentId">
              <el-select v-model="form.instrumentId" placeholder="请选择装置" clearable size="small"
                         style="width: 100%">
                <el-option
                  v-for="dict in instrumentList"
                  :key="dict.id"
                  :label="dict.name"
                  :value="dict.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="checked">
          <el-col :span="24">
            <el-form-item label="组件编码" prop="assemblyCode">
              <el-input v-model="form.assemblyCode" placeholder="请输入组件编码"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="环境温度(℃)" prop="ambientTemp">
              <el-input v-model="form.ambientTemp" placeholder="请输入环境温度" style="width: 85%"/> ℃
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="环境湿度" prop="ambientHumidity">
              <el-input v-model="form.ambientHumidity" placeholder="请输入环境湿度" style="width: 85%"/> %RH
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="风向" prop="windSpeed">
              <el-input v-model="form.windSpeed" placeholder="请输入风向"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风速(m/s)" prop="windDirection">
              <el-input v-model="form.windDirection" placeholder="请输入风速" style="width: 85%"/> m/s
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="检测人员" prop="tester">
              <el-input v-model="form.tester" placeholder="请输入检测人员"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检测日期" prop="testTime">
              <el-date-picker clearable
                              v-model="form.testTime"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="请选择检测日期"
                              style="width: 100%"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="位置1检测结果(ppm)" prop="position1" label-width="160px">
              <el-input-number v-model="form.position1" :precision="1" :step="0.1" @change="getChange"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="位置2检测结果(ppm)" prop="position2" label-width="160px">
              <el-input-number v-model="form.position2" :precision="1" :step="0.1" @change="getChange"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="位置3检测结果(ppm)" prop="position3" label-width="160px">
              <el-input-number v-model="form.position3" :precision="1" :step="0.1" @change="getChange"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="位置4检测结果(ppm)" prop="position4" label-width="160px">
              <el-input-number v-model="form.position4" :precision="1" :step="0.1" @change="getChange"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="位置5检测结果(ppm)" prop="position5" label-width="160px">
              <el-input-number v-model="form.position5" :precision="1" :step="0.1" @change="getChange"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="环境本底值计算结果(ppm)" prop="backgroundValue" label-width="195px">
              <el-input v-model="form.backgroundValue" disabled placeholder="0.0"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="大气压" prop="atmos">
              <el-input v-model="form.atmos" placeholder="请输入大气压"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注" prop="remarks">
              <el-input v-model="form.remarks" placeholder="请输入备注"/>
            </el-form-item>
          </el-col>
        </el-row>
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
  listEnvironment,
  getEnvironment,
  delEnvironment,
  addEnvironment,
  updateEnvironment
} from "@/api/check/environment";
import {getAllInstrument} from "@/api/check/instrument";
import {getAllPlantName} from "@/api/base/plant";
import {getToken} from "@/utils/auth";

export default {
  name: "Environment",
  data() {
    return {
      checked: false,
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
      // 用户导入参数
      upload: {
        downloadAction: process.env.VUE_APP_BASE_API + '/common/template',
        //下载模板类型
        type: "environment",
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
        url: process.env.VUE_APP_BASE_API + "/check/environment/importData"
      },
      chooseTime: [],
      plantOperation:[],
      instrumentList:[],
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
      // 环境本底值记录表格数据
      environmentList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        plantId: null,
        instrumentId: null,
        assemblyCode: null,
        backgroundValue: null,
        ambientTemp: null,
        ambientHumidity: null,
        atmos: null,
        windDirection: null,
        windSpeed: null,
        tester: null,
        testTime: null,
        remarks: null,
        deptId: null,
        createrCode: null,
        createdate: null,
        updaterCode: null,
        updatedate: null,
        startTime: null,
        endTime: null,

      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        plantId: [
          {required: true, message: '请选择装置', trigger: "blur"}
        ],
        instrumentId: [
          {required: true, message: '请选择仪器', trigger: "blur"}
        ],
        testTime: [
          {required: true, message: this.$t('检测日期') + this.$t('不能为空'), trigger: "blur"}
        ],
        assemblyCode: [
          {required: true, message: this.$t('组件编码') + this.$t('不能为空'), trigger: "blur"}
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
    getAllPlantName().then(response => {
      this.plantOperation = response.data;
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
    getChange(){
      this.form.backgroundValue = ((this.form.position1+this.form.position2+this.form.position3+this.form.position4+this.form.position5)/5).toFixed(1);
    },
    /** 查询环境本底值记录列表 */
    getList() {
      this.loading = true;
      listEnvironment(this.queryParams).then(response => {
        this.environmentList = response.rows;
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
        instrumentId: null,
        assemblyCode: null,
        backgroundValue: 0,
        ambientTemp: null,
        ambientHumidity: null,
        atmos: null,
        windDirection: null,
        windSpeed: null,
        tester: null,
        testTime: null,
        remarks: null,
        deptId: null,
        delFlag: null,
        createrCode: null,
        createdate: null,
        updaterCode: null,
        updatedate: null,
        position1: 0,
        position2: 0,
        position3: 0,
        position4: 0,
        position5: 0,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.queryParams.startTime = this.chooseTime[0];
      this.queryParams.endTime = this.chooseTime[1];
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
      this.title = "添加环境本底值记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getEnvironment(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改环境本底值记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateEnvironment(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addEnvironment(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除环境本底值记录编号为"' + ids + '"的数据项？').then(function () {
        return delEnvironment(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('check/environment/export', {
        ...this.queryParams
      }, `环境本底值清单_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
