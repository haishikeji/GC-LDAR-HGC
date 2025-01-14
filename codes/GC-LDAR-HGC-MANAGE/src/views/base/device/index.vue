<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="装置" prop="plantId">
        <el-select v-model="queryParams.plantId" @change="handleQuery"
                   placeholder="请选择装置" clearable size="small">
          <el-option
            v-for="plant in plantOperation"
            :key="plant.plantId"
            :label="plant.plantName"
            :value="plant.plantId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="设备/管线编号" prop="devCode" label-width="110px">
        <el-input
          v-model="queryParams.devCode"
          placeholder="请输入设备/管线编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备/管线描述" prop="devDescribe" label-width="110px">
        <el-input
          v-model="queryParams.devDescribe"
          placeholder="请输入设备/管线描述"
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
          v-hasPermi="['base:device:add']"
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
          v-hasPermi="['base:device:edit']"
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
          v-hasPermi="['base:device:remove']"
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
          v-hasPermi="['base:device:export']"
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
          v-hasPermi="['base:device:approve']"
        >审核
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="deviceList" :cell-style="tableCellStyle"
              @selection-change="handleSelectionChange" :height="clientHeight" border>
      <el-table-column type="selection" width="55" align="center" fixed="left"/>
      <el-table-column label="审核状态" align="center" fixed="left" prop="approveStatus"
                       :formatter="approveStatusFormat" :show-overflow-tooltip="true" width="80"/>
      <el-table-column label="装置名称" align="center" prop="plantName" :show-overflow-tooltip="true" width="180"/>
      <el-table-column label="装置编编码" align="center" prop="plantCode" :show-overflow-tooltip="true" width="100"/>
      <el-table-column label="区域名称" align="center" prop="regionName" :show-overflow-tooltip="true" width="150"/>
      <el-table-column label="区域编号" align="center" prop="regionCode" :show-overflow-tooltip="true" width="100"/>
      <el-table-column label="设备/管线编号" align="center" prop="devCode" :show-overflow-tooltip="true" width="130"/>
      <el-table-column label="设备/管线描述" align="center" prop="devDescribe" :show-overflow-tooltip="true"
                       width="230"/>
      <el-table-column label="主要物料/产品" align="center" prop="material" :show-overflow-tooltip="true" width="130"/>
      <el-table-column label="物料状态" align="center" prop="materialStatus" :show-overflow-tooltip="true" width="100"/>
      <el-table-column label="合成相应因子" align="center" prop="responseFactor" :show-overflow-tooltip="true"
                       width="100"/>
      <el-table-column label="相应因子来源" align="center" prop="responseFactorFrom" :show-overflow-tooltip="true"
                       width="100"/>
      <el-table-column label="维护人" align="center" prop="updater" :show-overflow-tooltip="true" width="100"/>
      <el-table-column label="维护时间" align="center" prop="updatedate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updatedate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" width="230"/>
      <el-table-column label="操作" align="center" fixed="right" class-name="small-padding fixed-width" width="120">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['base:device:edit']"
            v-if="scope.row.approveStatus==0"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['base:device:remove']"
            v-if="scope.row.approveStatus==0"
          >删除
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-circle-close"
            @click="handleDisabled(scope.row.devId)"
            v-hasPermi="['base:device:remove']"
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

    <!-- 添加或修改设备/管线对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="装置" prop="plantId">
          <el-select v-model="form.plantId" placeholder="请选择装置" clearable size="small" style="width: 100%"
                     @change="getAllRegion(form.plantId)">
            <el-option
              v-for="dict in plantOperation"
              :key="dict.plantId"
              :label="dict.plantName"
              :value="dict.plantId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="区域" prop="regionId">
          <el-select v-model="form.regionId" placeholder="请选择区域" clearable size="small" style="width: 100%">
            <el-option
              v-for="dict in regionOperation"
              :key="dict.regionId"
              :label="dict.regionName"
              :value="dict.regionId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="设备/管线编号" prop="devCode">
          <el-input v-model="form.devCode" placeholder="请输入设备/管线编号"/>
        </el-form-item>
        <el-form-item label="设备/管线描述" prop="devDescribe">
          <el-input v-model="form.devDescribe" placeholder="请输入设备/管线描述"/>
        </el-form-item>
        <el-form-item label="主要物料/产品" prop="material">
          <el-input v-model="form.material" placeholder="请输入主要物料/产品"/>
        </el-form-item>
        <el-form-item label="物料状态" prop="materialStatus">
          <el-input v-model="form.materialStatus" placeholder="请输入物料状态"/>
        </el-form-item>
        <el-form-item label="合成相应因子" prop="responseFactor">
          <el-input v-model="form.responseFactor" placeholder="请输入合成相应因子"/>
        </el-form-item>
        <el-form-item label="相应因子来源" prop="responseFactorFrom">
          <el-input v-model="form.responseFactorFrom" placeholder="请输入相应因子来源"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注"/>
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
import {addDevice, delDevice, getDevice, handleApprove, listDevice, updateDevice, handleDisabled} from "@/api/base/device";
import {getAllPlantName} from "@/api/base/plant";
import {getAllRegion} from "@/api/base/region";
import {MessageBox} from 'element-ui'
import {getToken} from "@/utils/auth";

export default {
  name: "Device",
  data() {
    return {
      plantOperation: [],
      regionOperation: [],
      approveStatusOperation: [],
      clientHeight: 300,
      // 遮罩层
      loading: true,
      // 选中数组l
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
      // 设备/管线表格数据
      deviceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      fullscreenLoading: false,
      // 用户导入参数
      upload: {
        downloadAction: process.env.VUE_APP_BASE_API + '/common/template',
        //下载模板类型
        type: "baseDevice",
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
        url: process.env.VUE_APP_BASE_API + "/base/device/importData"
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        plantId: null,
        regionId: null,
        devCode: null,
        devDescribe: null,
        approveStatus: null,
        approveTime: null,
        deptId: null,
        createrCode: null,
        createdate: null,
        updaterCode: null,
        updatedate: null,
        responseFactorFrom: null,
        responseFactor: null,
        materialStatus: null,
        material: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        regionId: [
          {required: true, message: this.$t('区域') + this.$t('不能为空'), trigger: "blur"}
        ],
        plantId: [
          {required: true, message: this.$t('装置') + this.$t('不能为空'), trigger: "blur"}
        ],
        devDescribe: [
          {required: true, message: this.$t('描述') + this.$t('不能为空'), trigger: "blur"}
        ],
        devCode: [
          {required: true, message: this.$t('编号') + this.$t('不能为空'), trigger: "blur"}
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
    })
    this.getDicts("base_approve_status").then(response => {
      this.approveStatusOperation = response.data;
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
    approveStatusFormat(row, column) {
      return this.selectDictLabel(this.approveStatusOperation, row.approveStatus);
    },
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
    getAllRegion(val) {
      this.form.regionId = '';
      if (val != null && val != '') {
        getAllRegion(val).then(response => {
            this.regionOperation = response.data;
          }
        )
      } else {
        this.regionOperation = [];
      }
    },
    /** 查询设备/管线列表 */
    getList() {
      this.loading = true;
      listDevice(this.queryParams).then(response => {
        this.deviceList = response.rows;
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
        devId: null,
        plantId: null,
        regionId: null,
        devCode: null,
        devDescribe: null,
        remark: null,
        approveStatus: 0,
        approveTime: null,
        deptId: null,
        delFlag: null,
        createrCode: null,
        createdate: null,
        updaterCode: null,
        updatedate: null,
        responseFactorFrom: null,
        responseFactor: null,
        materialStatus: null,
        material: null
      };
      this.resetForm("form");
      this.regionOperation = [];
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
      this.ids = selection.map(item => item.devId)
      this.status = selection.map(item => item.approveStatus)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加设备/管线";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const devId = row.devId || this.ids
      getDevice(devId).then(response => {
        this.form = response.data;
        if (this.form.approveStatus != 0) {
          MessageBox.alert('已送审/已审核的数据不可修改！', '注意！', {
            confirmButtonText: '确定',
          })
          return;
        }
        getAllRegion(this.form.plantId).then(response => {
            this.regionOperation = response.data;
            console.log(this.regionOperation)
          }
        )
        this.open = true;
        this.title = "修改设备/管线";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.devId != null) {
            updateDevice(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDevice(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleDisabled(id){
      this.$modal.confirm('是否确认作废数据项？注意！该操作不可逆！作废后该设备下的所有密封点都将一并作废！').then(function () {
        return handleDisabled(id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("作废成功");
      }).catch(() => {
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.devId || this.ids;
      if (row.approveStatus === undefined) {
        for (let i = 0; i < this.status.length; i++) {
          if (this.status[i] != 0) {
            MessageBox.alert('已送审/已审核的数据不可删除！', '注意！', {
              confirmButtonText: '确定',
            })
            return
          }
        }
      } else if (row.approveStatus != 0) {
        MessageBox.alert('已送审/已审核的数据不可删除！', '注意！', {
          confirmButtonText: '确定',
        })
        return
      }
      this.$modal.confirm('是否确认删除数据项？').then(function () {
        return delDevice(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('base/device/export', {
        ...this.queryParams
      }, `受控设备台账_${new Date().getTime()}.xlsx`)
    },
    handleToApprove() {
      let data = {};
      data.devIds = this.ids;
      data.approveStatus = 1;
      handleApprove(data).then(() => {
        this.getList();
      })
    },
    handleApprove() {
      let data = {};
      data.devIds = this.ids;
      data.approveStatus = 2;
      handleApprove(data).then(() => {
        this.getList();
      })
    }
  }
};
</script>
