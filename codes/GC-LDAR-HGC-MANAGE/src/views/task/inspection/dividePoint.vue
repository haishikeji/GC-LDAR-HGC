<template xmlns="http://www.w3.org/1999/html">
  <div>
      <el-dialog :visible.sync="dialog.dialogFormVisible" width="90%" :close-on-click-modal="false"
                 title='添加检测点'>
        <el-row>
          <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="68px">
            <el-form-item label="装置" prop="plantId">
              <el-select v-model="queryParams.plantId" @change="handleQuery"
                         placeholder="请选择装置" clearable size="small" disabled>
                <el-option
                  v-for="plant in plantOperation"
                  :key="plant.plantId"
                  :label="plant.plantName"
                  :value="plant.plantId"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="群组编码" prop="groupCode">
              <el-input
                v-model="queryParams.groupCode"
                placeholder="请输入群组编码"
                clearable
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
            <el-form-item label="密封点类型" prop="pointType" label-width="90px">
              <el-select v-model="queryParams.pointType" @change="handleQuery" placeholder="请选择密封点类型" clearable
                         size="small">
                <el-option
                  v-for="dict in pointOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </el-row>
        <el-row>

          <el-col :span="11" style="padding-right: 5px;text-align: center">
            <el-table :data="leftData" style="width: 100%;" height="530px" border @selection-change="saveLeft"
                      ref="leftData">
              <el-table-column type="selection" align="center" fixed="left"></el-table-column>
              <!--            <el-table-column label="检测值" align="center" prop="testValue" v-if="!checkAgain" width="80"/>
                          <el-table-column label="复测值" align="center" prop="testValue" v-else width="80"/>
                          <el-table-column label="净检测值" align="center" prop="netTestValue" width="80"/>-->
              <el-table-column label="装置名称" align="center" prop="plantName" width="100"
                               :show-overflow-tooltip="true"/>
              <el-table-column label="区域名称" align="center" prop="regionName" width="100"
                               :show-overflow-tooltip="true"/>
              <el-table-column label="平台" align="center" prop="layer" width="100" :show-overflow-tooltip="true"/>
              <el-table-column label="设备/管线名称" align="center" prop="devName" width="130"
                               :show-overflow-tooltip="true"/>
              <el-table-column label="设备/管线编号" align="center" prop="devCode" width="130"
                               :show-overflow-tooltip="true"/>
              <el-table-column label="群组编码" align="center" prop="groupCode" width="100"
                               :show-overflow-tooltip="true"/>
              <el-table-column label="密封点扩展号编码" align="center" prop="extendCode" width="150"
                               :show-overflow-tooltip="true"/>
              <el-table-column label="密封点类型" align="center" prop="pointType" width="100"
                               :show-overflow-tooltip="true"/>
              <!--            <el-table-column label="仪器编号" align="center" prop="inspectionCode" width="100"
                                           :show-overflow-tooltip="true"/>
                          <el-table-column label="泄漏部位" align="center" prop="leakagePosition" width="100"
                                           :show-overflow-tooltip="true"/>
                          <el-table-column label="校准人员" align="center" prop="checker" width="100" :show-overflow-tooltip="true"/>
                          <el-table-column label="校准日期" align="center" prop="checkDate" width="100"
                                           :show-overflow-tooltip="true"/>
                          <el-table-column label="泄漏程度" align="center" prop="leakageDegree" width="100"
                                           :show-overflow-tooltip="true"/>-->
            </el-table>
            <pagination
              v-show="leftTotal>0"
              :total="leftTotal"
              :page.sync="queryParams.pageNum"
              :limit.sync="queryParams.pageSize"
              @pagination="getList"
            />
          </el-col>

          <el-col :span="2" style="margin-top: 10%;text-align: center">
            <el-tooltip class="item" effect="dark" content="添加检查点" placement="right">
              <el-button type="primary" icon="el-icon-arrow-right" :disabled="leftMultiple" @click="add"
                         style="margin-bottom: 10px;width: 80%"
                         size="large"></el-button>
            </el-tooltip>
            <br>
            <el-tooltip class="item" effect="dark" content="移除检查点" placement="right">
              <el-button type="primary" icon="el-icon-arrow-left" :disabled="rightMultiple" @click="remove"
                         style="margin-top: 10px;margin-bottom: 10px;width: 80%"
                         size="large"></el-button>
            </el-tooltip>
            <br>
            <el-tooltip class="item" effect="dark" content="添加所有检查点" placement="right">
              <el-button type="primary" icon="el-icon-d-arrow-right" :disabled="leftAll" @click="addAll"
                         style="margin-top: 10px;margin-bottom: 10px;width: 80%"
                         size="large"></el-button>
            </el-tooltip>
            <br>
            <el-tooltip class="item" effect="dark" content="移除所有检查点" placement="right">
              <el-button type="primary" icon="el-icon-d-arrow-left" :disabled="rightAll" @click="removeAll"
                         style="margin-top: 10px;width: 80%"
                         size="large"></el-button>
            </el-tooltip>
          </el-col>

          <el-col :span="11" style="padding-left: 5px;text-align: center">
            <el-table :data="rightData" style="width: 100%" height="530px" border ref="rightData"
                      @selection-change="saveRight">
              <el-table-column type="selection" align="center" fixed="left"></el-table-column>
              <!--            <el-table-column label="检测值" align="center" prop="testValue" v-if="!checkAgain" width="80"/>
                          <el-table-column label="复测值" align="center" prop="testValue" v-else width="80"/>
                          <el-table-column label="净检测值" align="center" prop="netTestValue" width="80"/>-->
              <el-table-column label="装置名称" align="center" prop="plantName" width="100"
                               :show-overflow-tooltip="true"/>
              <el-table-column label="区域名称" align="center" prop="regionName" width="100"
                               :show-overflow-tooltip="true"/>
              <el-table-column label="平台" align="center" prop="layer" width="100" :show-overflow-tooltip="true"/>
              <el-table-column label="设备/管线名称" align="center" prop="devName" width="130"
                               :show-overflow-tooltip="true"/>
              <el-table-column label="设备/管线编号" align="center" prop="devCode" width="130"
                               :show-overflow-tooltip="true"/>
              <el-table-column label="群组编码" align="center" prop="groupCode" width="100"
                               :show-overflow-tooltip="true"/>
              <el-table-column label="密封点扩展号编码" align="center" prop="extendCode" width="150"
                               :show-overflow-tooltip="true"/>
              <el-table-column label="密封点类型" align="center" prop="pointType" width="100"
                               :show-overflow-tooltip="true"/>
              <!--            <el-table-column label="仪器编号" align="center" prop="inspectionCode" width="100"
                                           :show-overflow-tooltip="true"/>
                          <el-table-column label="泄漏部位" align="center" prop="leakagePosition" width="100"
                                           :show-overflow-tooltip="true"/>
                          <el-table-column label="校准人员" align="center" prop="checker" width="100" :show-overflow-tooltip="true"/>
                          <el-table-column label="校准日期" align="center" prop="checkDate" width="100"
                                           :show-overflow-tooltip="true"/>
                          <el-table-column label="泄漏程度" align="center" prop="leakageDegree" width="100"
                                           :show-overflow-tooltip="true"/>-->
            </el-table>
            <pagination
              v-show="rightTotal>0"
              :total="rightTotal"
              :page.sync="rightQueryParams.pageNum"
              :limit.sync="rightQueryParams.pageSize"
              @pagination="getRightList"
            />
          </el-col>

        </el-row>

        <div slot="footer" class="dialog-footer" style="text-align: center">
          <el-button @click="cancel">返回</el-button>
        </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listCheckpoints, updateCheckpoints,
} from "@/api/check/checkpoints";
import {getAllPlantName} from "@/api/base/plant";

export default {
  name: "dividePoint",
  data() {
    return {
      leftTotal: 0,
      rightTotal: 0,
      checkAgain: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        pointId: null,
        planId: null,
        inspectionId: null,
        testValue: null,
        netTestValue: null,
        plantId: null,
        plantName: null,
        regionId: null,
        regionName: null,
        layer: null,
        devId: null,
        devName: null,
        devCode: null,
        groupCode: null,
        extendCode: null,
        pointType: null,
        inspectionCode: null,
        leakagePosition: null,
        checker: null,
        checkId: null,
        checkDate: null,
        leakageDegree: null,
        remarks: null,
        approveStatus: null,
        approveTime: null,
        deptId: null,
        createrCode: null,
        createdate: null,
        updaterCode: null,
        updatedate: null,
        reqairId: null,
        choose: 1,
      },
      // 查询参数
      rightQueryParams: {
        pageNum: 1,
        pageSize: 10,
        pointId: null,
        planId: null,
        inspectionId: null,
        testValue: null,
        netTestValue: null,
        plantId: null,
        plantName: null,
        regionId: null,
        regionName: null,
        layer: null,
        devId: null,
        devName: null,
        devCode: null,
        groupCode: null,
        extendCode: null,
        pointType: null,
        inspectionCode: null,
        leakagePosition: null,
        checker: null,
        checkId: null,
        checkDate: null,
        leakageDegree: null,
        remarks: null,
        approveStatus: null,
        approveTime: null,
        deptId: null,
        createrCode: null,
        createdate: null,
        updaterCode: null,
        updatedate: null,
        choose: null,
      },
      row: {},
      rightData: [],
      leftData: [],
      right: [],   //右边选中的数据
      left: [],  //左边选中的数据
      leftMultiple: true,
      rightMultiple: true,
      leftAll: this.leftData == undefined || this.leftData == null || this.leftData.length <= 0,
      rightAll: this.rightData == undefined || this.rightData == null || this.rightData.length <= 0,
      dialog: {
        dialogFormVisible: false,
      },
      inspectionId: null,
      pointOptions: [],
      plantOperation: [],
    }
  },
  methods: {
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    pointFormat(row, column) {
      return this.selectDictLabel(this.pointOptions, row.pointType);
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    cancel() {
      this.leftData = [];
      this.rightData = [];
      this.dialog.dialogFormVisible = false;
    },
    openDialog(row) {
      this.leftData = [];
      this.rightData = [];
      this.row = row;
      this.rightQueryParams.inspectionId = row.id;
      this.queryParams.plantId = row.plantId;
      this.dialog.dialogFormVisible = true;
      this.checkAgain = row.taskType == 2 ? true : false;
      this.getList();
      this.getRightList();
      this.getDicts("point_type").then(response => {
        this.pointOptions = response.data;
      });
      getAllPlantName().then(response => {
        this.plantOperation = response.data;
      })
    },
    /** 查询检测点列表 */
    getList() {
      if (this.row.taskType==2){
        this.queryParams.repairId=0;
      }else {
        this.queryParams.repairId = null;
      }
      this.queryParams.inspectionId = null;
      this.queryParams.planId = this.row.planId;
      listCheckpoints(this.queryParams).then(response => {
        this.leftData = response.rows;
        this.leftTotal = response.total;
        this.leftAll = this.leftData == undefined || this.leftData == null || this.leftData.length <= 0;
      });
    },
    getRightList() {
      if (this.row.taskType==2){
        this.rightQueryParams.repairId=0;
      }else {
        this.rightQueryParams.repairId = null;
      }
      listCheckpoints(this.rightQueryParams).then(response => {
        this.rightData = response.rows;
        this.rightTotal = response.total;
        this.rightAll = this.rightData == undefined || this.rightData == null || this.rightData.length <= 0;
      });
    },
    saveLeft(rows) {
      this.left = [];
      console.log(rows)
      if (rows) {
        this.left = rows.map(row => row.checkId);
        this.leftMultiple = !rows.length;
      }
    },
    saveRight(rows) {
      this.right = [];
      if (rows) {
        this.right = rows.map(row => row.checkId);
        this.rightMultiple = !rows.length;
      }
    },
    // 左边表格选择项移到右边
    add() {
      let data = {};
      data.inspectionId = this.rightQueryParams.inspectionId;
      data.checkIds = this.left;
      data.choose = 1;
      console.log(data)
      updateCheckpoints(data).then(response => {
        this.getList();
        this.getRightList();
      })
    },
    addAll() {
      if (this.leftData) {
        let data = this.queryParams;
        data.inspectionId = this.rightQueryParams.inspectionId;
        data.choose = 3;
        if (this.row.taskType==2){
          this.queryParams.repairId=0;
        }else {
          this.queryParams.repairId = null;
        }
        updateCheckpoints(data).then(response => {
          this.getList();
          this.getRightList();
        })
      }
    },
    // 右边表格选择项移到左边
    remove() {
      let data = {};
      data.inspectionId = this.rightQueryParams.inspectionId;
      data.checkIds = this.right;
      data.choose = 2;
      console.log(data)
      updateCheckpoints(data).then(response => {
        this.getList();
        this.getRightList();
      })
    },
    removeAll() {
      if (this.rightData !== []) {
        let data = {};
        data.inspectionId = this.rightQueryParams.inspectionId;
        data.choose = 4;
        console.log(data)
        updateCheckpoints(data).then(response => {
          console.log("this.queryParams")
          console.log(this.queryParams)
          this.getList();
          this.getRightList();
        })
      }
    }
  }
}
</script>
