<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="法规名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入法规名称"
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
          v-hasPermi="['check:laws:add']"
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
          v-hasPermi="['check:laws:edit']"
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
          v-hasPermi="['check:laws:remove']"
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
          v-hasPermi="['check:laws:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="lawsList" @selection-change="handleSelectionChange" :height="clientHeight"
              border>
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="法规编号" align="center" prop="code" :show-overflow-tooltip="true" width="180"/>
      <el-table-column label="法规名称" align="center" prop="name" :show-overflow-tooltip="true"  width="400"/>
      <el-table-column label="法规类型" align="center" prop="type" :show-overflow-tooltip="true" width="180"/>
      <el-table-column label="匹配状态" align="center" prop="status" :formatter="matchStatusFormat" width="180"/>
      <el-table-column label="匹配开始时间" align="center" prop="starttime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.starttime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remarks" width="180"/>
      <el-table-column label="维护人" align="center" prop="updater" width="180"/>
      <el-table-column label="维护时间" align="center" prop="updatedate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updatedate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180" fixed="right">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-connection"
            @click="handleMatch(scope.row)"
            v-hasPermi="['check:laws:edit']"
          >匹配
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['check:laws:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['check:laws:remove']"
          >删除
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-tickets"
            @click="getLawItems(scope.row)"
            v-hasPermi="['check:laws:edit']"
          >法规项
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

    <!-- 添加或修改法规对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="法规编号" prop="code">
          <el-input v-model="form.code" placeholder="请输入法规编号"/>
        </el-form-item>
        <el-form-item label="法规名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入法规名称"/>
        </el-form-item>
        <el-form-item label="法规类型" prop="type">
          <el-input v-model="form.type" placeholder="请输入法规类型"/>
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
    <lawitems v-if="itemsVisible" ref="itemsList"></lawitems>
  </div>
</template>

<script>
import {addLaws, delLaws, getLaws, listLaws, matchLaws, updateLaws} from "@/api/check/laws";
import {MessageBox} from "element-ui";
import Lawitems from "@/views/check/lawitems";

export default {
  name: "Laws",
  components: {Lawitems},
  data() {
    return {
      itemsVisible: false,
      matchOperation: [],
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
      // 法规表格数据
      lawsList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        code: null,
        name: null,
        type: null,
        status: null,
        starttime: null,
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
        code: [
          {required: true, message: '法规编码不能为空', trigger: "blur"}
        ],
        name: [
          {required: true, message: '法规名称不能为空', trigger: "blur"}
        ],
        type: [
          {required: true, message: '法规类型不能为空', trigger: "blur"}
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
    this.getDicts("match_status").then(response => {
      this.matchOperation = response.data;
    })
  },
  methods: {
    getLawItems(row){
      this.itemsVisible = true;
      this.$nextTick(() => {
        this.$refs.itemsList.init(row)

      })
    },
    matchStatusFormat(row, column) {
      return this.selectDictLabel(this.matchOperation, row.status);
    },
    /** 查询法规列表 */
    getList() {
      this.loading = true;
      listLaws(this.queryParams).then(response => {
        this.lawsList = response.rows;
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
        code: null,
        name: null,
        type: null,
        status: "0",
        starttime: null,
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
      this.status = selection.map(item => item.status)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加法规";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getLaws(id).then(response => {
        this.form = response.data;
        if (this.form.status==1){
          MessageBox.alert('匹配中的法规不可修改！', '注意！', {
            confirmButtonText: '确定',
          })
          return;
        }
        this.open = true;
        this.title = "修改法规";
      });
    },
    handleMatch(row) {
      if (row.status==1){
        MessageBox.alert('当前已匹配此法规！', '注意！', {
          confirmButtonText: '确定',
        })
        return;
      }
      this.reset();
      this.$modal.confirm('确认匹配此法规？').then(function () {
        return matchLaws({status:"1",id:row.id});
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("匹配成功");
      }).catch(() => {
        this.$modal.msg("取消匹配");
      });

    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateLaws(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addLaws(this.form).then(response => {
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
      if(row.status===undefined){
        for (let i = 0; i < this.status.length; i++) {
          if (this.status[i] ==1) {
            MessageBox.alert('匹配中的法规不可删除！', '注意！', {
              confirmButtonText: '确定',
            })
            return
          }
        }
      }else if (row.status==1){
        MessageBox.alert('匹配中的数据不可删除！', '注意！', {
          confirmButtonText: '确定',
        })
        return
      }
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除数据项？').then(function () {
        return delLaws(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('check/laws/export', {
        ...this.queryParams
      }, `法律法规_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
