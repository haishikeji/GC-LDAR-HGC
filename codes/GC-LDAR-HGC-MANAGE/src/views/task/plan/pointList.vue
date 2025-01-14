<template>
  <div>
    <el-dialog width="90%" :visible.sync="dialogFormVisible" :close-on-click-modal="false" title='计划检测点清单'>
      <el-table v-loading="loading" :data="pointList" border max-height="650">
        <el-table-column type="index" width="55" align="center"/>
        <el-table-column label="装置名称" align="center" prop="plantName" :show-overflow-tooltip="true"/>
        <el-table-column label="装置编码" align="center" prop="plantCode" :show-overflow-tooltip="true"/>
        <el-table-column label="装置类别" align="center" prop="plantType" :formatter="plantTypeFormat"/>
        <el-table-column label="区域名称" align="center" prop="regionName" :show-overflow-tooltip="true"/>
        <el-table-column label="区域编码" align="center" prop="regionCode" :show-overflow-tooltip="true"/>
        <el-table-column label="设备/管线名称" align="center" prop="devName" width="150" :show-overflow-tooltip="true"/>
        <el-table-column label="设备/管线编码" align="center" prop="devCode" width="150" :show-overflow-tooltip="true"/>
        <el-table-column label="介质" align="center" prop="medium" :show-overflow-tooltip="true"/>
        <el-table-column label="介质状态" align="center" prop="mediumType" :formatter="mediumTypeFormat"
                         :show-overflow-tooltip="true"/>
        <el-table-column label="密封点类型" align="center" prop="pointType" width="100"
                         :show-overflow-tooltip="true"/>
        <el-table-column label="平台(层)" align="center" prop="layer" :show-overflow-tooltip="true"/>
        <el-table-column label="群组位置" align="center" prop="groupPosition" :show-overflow-tooltip="true"/>
        <el-table-column label="密封点位置" align="center" prop="pointPosition" width="110"
                         :show-overflow-tooltip="true"/>
        <el-table-column label="群组编码" align="center" prop="groupCode" :show-overflow-tooltip="true"/>
        <el-table-column label="扩展编码" align="center" prop="extendCode" :show-overflow-tooltip="true"/>
        <el-table-column label="密封点子类型" align="center" prop="subPointType" width="110"
                         :show-overflow-tooltip="true"/>
        <el-table-column label="公称直径(mm)" align="center" prop="dia" width="110" :show-overflow-tooltip="true"/>
        <el-table-column label="是否不可达点" align="center" prop="unarrive" width="110"
                         :show-overflow-tooltip="true"/>
        <el-table-column label="不可达原因" align="center" prop="unarriveReason" width="110"
                         :show-overflow-tooltip="true"/>
        <el-table-column label="是否保温/保冷" align="center" prop="keepWarm" width="110"
                         :show-overflow-tooltip="true"/>
        <el-table-column label="工艺温度(℃)" align="center" prop="temperature" width="110"
                         :show-overflow-tooltip="true"/>
        <el-table-column label="工艺压力(Mpa)" align="center" prop="pressure" width="110"
                         :show-overflow-tooltip="true"/>
        <el-table-column label="运行时间" align="center" prop="runTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.runTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="TOC质量分数" align="center" prop="tocMark" width="110" :show-overflow-tooltip="true"/>
        <el-table-column label="甲烷质量分数" align="center" prop="methaneMark" width="110"
                         :show-overflow-tooltip="true"/>
        <el-table-column label="VOCs质量分数" align="center" prop="vocsMark" width="110" :show-overflow-tooltip="true"/>
        <el-table-column label="描述" align="center" prop="remarks" width="180" :show-overflow-tooltip="true"/>
      </el-table>


      <div style="height: 50px">
        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listByPlan} from "@/api/base/point";

export default {
  name: "pointList",
  data() {
    return {
      dialogFormVisible: false,
      pointList: [],
      plantTypeOptions: [],
      mediumTypeOptions: [],
      loading: false,
      row: {},
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        planId: null,
      }
    }
  },
  methods: {
    init(row) {
      this.pointList= [],
      this.dialogFormVisible=true;
      this.row = row;
      this.getDicts("plant_type").then(response => {
        this.plantTypeOptions = response.data;
      });

      this.getDicts("medium_type").then(response => {
        this.mediumTypeOptions = response.data;
      });
      this.queryParams.planId = row.id;
      this.getList()
    },
    getList() {
      this.loading = true;
      listByPlan(this.queryParams).then(res => {
        this.pointList = res.rows;
        this.total = res.total;
        this.loading = false;
      })
    },
    mediumTypeFormat(row, column) {
      return this.selectDictLabel(this.mediumTypeOptions, row.mediumType);
    },
    plantTypeFormat(row, column) {
      return this.selectDictLabel(this.plantTypeOptions, row.plantType);
    },
  }
}
</script>

<style scoped>

</style>
