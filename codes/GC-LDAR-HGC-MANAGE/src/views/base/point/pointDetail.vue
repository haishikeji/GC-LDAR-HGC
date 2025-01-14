<template>
  <div>
    <el-dialog title="密封点详情" :visible.sync="open" width="85%" append-to-body>
      <el-descriptions :column="3" border size="medium" style="margin-bottom: 20px">
        <el-descriptions-item labelStyle="width: 12%" contentStyle="width: 21%">
          <template slot="label">
            装置
          </template>
          <div v-for="item in plantOperation" :key="item.plantId">
            <div v-if="item.plantId==form.plantId">
              {{ item.plantName }}
            </div>
          </div>
        </el-descriptions-item>
        <el-descriptions-item labelStyle="width: 12%" contentStyle="width: 21%">
          <template slot="label">
            区域
          </template>
          <div v-for="item in regionOperation" :key="item.regionId">
            <div v-if="item.regionId==form.regionId">
              {{ item.regionName }}
            </div>
          </div>
        </el-descriptions-item>
        <el-descriptions-item labelStyle="width: 12%" contentStyle="width: 21%">
          <template slot="label">
            设备
          </template>
          <div v-for="item in deviceOperation" :key="item.devId">
            <div v-if="item.devId==form.devId">
              {{ item.devDescribe }}
            </div>
          </div>
        </el-descriptions-item>
        <el-descriptions-item labelStyle="width: 12%" contentStyle="width: 21%">
          <template slot="label">
            介质状态
          </template>
          <div v-for="item in mediumTypeOptions" :key="item.dictValue">
            <div v-if="item.dictValue==form.mediumType">
              {{ item.dictLabel }}
            </div>
          </div>
        </el-descriptions-item>
        <el-descriptions-item labelStyle="width: 12%" contentStyle="width: 21%">
          <template slot="label">
            密封点类型
          </template>
          {{ this.form.pointType }}
        </el-descriptions-item>
        <el-descriptions-item labelStyle="width: 12%" contentStyle="width: 21%">
          <template slot="label">
            介质
          </template>
          {{ this.form.medium }}
        </el-descriptions-item>
        <el-descriptions-item labelStyle="width: 12%" contentStyle="width: 21%">
          <template slot="label">
            群组位置
          </template>
          {{ this.form.groupPosition }}
        </el-descriptions-item>
        <el-descriptions-item labelStyle="width: 12%" contentStyle="width: 21%">
          <template slot="label">
            群组编码
          </template>
          {{ this.form.groupCode }}
        </el-descriptions-item>
        <el-descriptions-item labelStyle="width: 12%" contentStyle="width: 21%">
          <template slot="label">
            扩展编码
          </template>
          {{ this.form.extendCode }}
        </el-descriptions-item>
      </el-descriptions>
      <div>
        <el-table :data="pointList" border :cell-style="tableCellStyle" height="450">
          <el-table-column label="密封点检测记录" align="center">
            <el-table-column label="检测值(ppm)" align="center" prop="testValue" :show-overflow-tooltip="true"/>
            <el-table-column label="净检测值(ppm)" align="center" prop="netTestValue" :show-overflow-tooltip="true"/>
            <el-table-column label="泄漏程度" align="center" prop="leakageDegree" :formatter="leakageFormat"
                             :show-overflow-tooltip="true"/>
            <el-table-column label="检测人员" align="center" prop="checker"
                             :show-overflow-tooltip="true"/>
            <el-table-column label="检测日期" align="center" prop="checkDate">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.checkDate, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
          </el-table-column>
        </el-table>
        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">返 回</el-button>
      </div>

    </el-dialog>
  </div>
</template>

<script>
import {getPoint} from "@/api/base/point";
import {getAllRegion} from "@/api/base/region";
import {getAllDeviceByRegionId} from "@/api/base/device";
import {listCheckpoints} from "@/api/check/checkpoints";

export default {
  name: "pointDetail",
  data() {
    return {
      total: 0,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        pointId: null,
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
      // 是否显示弹出层
      open: false,
      pointList: [],
      plantOperation: [],
      regionOperation: [],
      deviceOperation: [],
      yesOrNoOperation: [],
      pointOptions: [],
      mediumTypeOptions: [],
      // 表单参数
      form: {},
      row: {},
      // 表单校验
      rules: {
        plantId: [
          {required: true, message: this.$t('请选择') + this.$t('装置'), trigger: "blur"}
        ],
        regionId: [
          {required: true, message: this.$t('请选择') + this.$t('区域'), trigger: "blur"}
        ],
        devId: [
          {required: true, message: this.$t('请选择') + this.$t('设备'), trigger: "blur"}
        ],
        groupCode: [
          {required: true, message: this.$t('群组编码') + this.$t('不能为空'), trigger: "blur"}
        ],
        extendCode: [
          {required: true, message: this.$t('密封点扩展号编码') + this.$t('不能为空'), trigger: "blur"}
        ],
        pointType: [
          {required: true, message: this.$t('请选择') + this.$t('密封点类型'), trigger: "blur"}
        ],
        unarrive: [
          {required: true, message: this.$t('请选择') + this.$t('是否不可达点'), trigger: "blur"}
        ],
        mediumType: [
          {required: true, message: this.$t('请选择') + this.$t('介质状态'), trigger: "blur"}
        ],
      }
    }
  },
  methods: {
    tableCellStyle({row, column, rowIndex, columnIndex}) {
      if (columnIndex === 2 && row.leakageDegree == 1) {
        return "color:#00ff00;font-size:200%";
      }
      if (columnIndex === 2 && row.leakageDegree == 2) {
        return "color:#FFDF00;font-size:200%";
      }
      if (columnIndex === 2 && row.leakageDegree == 3) {
        return "color:#ff0000;font-size:200%";
      }
    },
    leakageFormat(row, column) {
      return row.leakageDegree ? "●" : null;
    },
    init(row) {
      this.row = row;
      this.mediumTypeOptions = row.mediumTypeOptions;
      this.plantOperation = row.plantOperation;
      this.reset();
      getPoint(row.pointId).then(response => {
        this.form = response.data;
        getAllRegion(this.form.plantId).then(response => {
            this.regionOperation = response.data;
          }
        );
        getAllDeviceByRegionId(this.form.regionId).then(response => {
            this.deviceOperation = response.data;
          }
        )
        this.open = true;
      });
      this.getList();
    },
    getList() {
      this.queryParams.pointId = this.row.pointId;
      this.queryParams.approveStatus = 2;
      this.queryParams.repairId = 1;
      listCheckpoints(this.queryParams).then(response => {
        this.pointList = response.rows
        this.total=response.total
      })
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        pointId: null,
        netTestValue: null,
        leakageDegree: null,
        plantId: null,
        regionId: null,
        devId: null,
        medium: null,
        mediumType: null,
        pointType: null,
        layer: null,
        groupPosition: null,
        pointPosition: null,
        groupCode: null,
        extendCode: null,
        subPointType: null,
        dia: null,
        unarrive: null,
        unarriveReason: null,
        keepWarm: null,
        temperature: null,
        pressure: null,
        runTime: null,
        pidNo: null,
        pidUrl: null,
        picNo: null,
        picUrl: null,
        tocMark: null,
        methaneMark: null,
        vocsMark: null,
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
      this.pointList = [];
    },
  }
}
</script>

<style scoped>

</style>
