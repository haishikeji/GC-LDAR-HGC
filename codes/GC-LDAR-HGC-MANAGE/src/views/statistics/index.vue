<template>
  <div style="padding: 5px">
    <el-row>
      <el-card style="height: 70px">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="68px">
          <el-form-item label="装置" prop="plantId">
            <el-select v-model="queryParams.plantId" placeholder="请选择装置" clearable size="small"
                       @change="handleQuery">
              <el-option
                v-for="dict in plantOperation"
                :key="dict.plantId"
                :label="dict.plantName"
                :value="dict.plantId"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="统计年份" prop="year">
            <el-date-picker
              clearable
              v-model="year"
              @change="handleQuery"
              type="year">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="归属机构" prop="company">
            <el-input v-model="queryParams.company" placeholder="请输入归属机构" style="width: 200px"
                      @input="handleQuery"/>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">统计</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </el-row>
    <el-row>
      <el-card>
        <div class="bt"><span><i class="el-icon-coin i"></i> 基本信息统计</span></div>
        <el-table :data="statisticsList" border>
          <el-table-column type="index" align="center" width="50"/>
          <el-table-column label="装置名称" align="center" prop="plantName" width="130" :show-overflow-tooltip="true"/>
          <el-table-column label="装置(个)" align="center" prop="plantCount" width="80" :show-overflow-tooltip="true"/>
          <el-table-column label="设备(个)" align="center" prop="devCount" width="80" :show-overflow-tooltip="true"/>
          <el-table-column label="密封点信息" align="center">
            <el-table-column label="泵(个)" align="center" prop="bCount" width="80" :show-overflow-tooltip="true"/>
            <el-table-column label="压缩机(个)" align="center" prop="ysjCount" width="90"
                             :show-overflow-tooltip="true"/>
            <el-table-column label="阀门(个)" align="center" prop="fmCount" width="80" :show-overflow-tooltip="true"/>
            <el-table-column label="泄压设备(个)" align="center" prop="xysbCount" width="100"
                             :show-overflow-tooltip="true"/>
            <el-table-column label="连接件(个)" align="center" prop="ljjCount" width="90"
                             :show-overflow-tooltip="true"/>
            <el-table-column label="法兰(个)" align="center" prop="flCount" width="80" :show-overflow-tooltip="true"/>
            <el-table-column label="开口阀或开口管线(个)" align="center" prop="kkfCount" width="150"
                             :show-overflow-tooltip="true"/>
            <el-table-column label="取样连接系统(个)" align="center" prop="qyljCount" width="130"
                             :show-overflow-tooltip="true"/>
            <el-table-column label="其他(个)" align="center" prop="qtCount" width="80" :show-overflow-tooltip="true"/>
            <el-table-column label="合计(个)" align="center" prop="pointCount" width="80"
                             :show-overflow-tooltip="true"/>
          </el-table-column>
          <el-table-column label="泄漏程度信息" align="center">
            <el-table-column label="未泄漏(个)" align="center" prop="wxlCount" width="100"
                             :show-overflow-tooltip="true"/>
            <el-table-column label="一般泄漏(个)" align="center" prop="ybxlCount" width="100"
                             :show-overflow-tooltip="true"/>
            <el-table-column label="严重泄漏(个)" align="center" prop="yzxlCount" width="100"
                             :show-overflow-tooltip="true"/>
            <el-table-column label="已修复(个)" align="center" prop="yxfCount" width="100"
                             :show-overflow-tooltip="true"/>
            <el-table-column label="未修复(个)" align="center" prop="wxfCount" width="100"
                             :show-overflow-tooltip="true"/>
<!--            <el-table-column label="未检测(个)" align="center" prop="wjcCount" width="100"-->
<!--                             :show-overflow-tooltip="true"/>-->
            <el-table-column label="合计(个)" align="center" prop="xlcdCount" width="80" :show-overflow-tooltip="true"/>
          </el-table-column>
          <el-table-column label="排放量核算" align="center">
            <el-table-column label="泄漏量(t/a)" align="center" prop="xll" width="130"
                             :show-overflow-tooltip="true"/>
            <el-table-column label="减排量(t/a)" align="center" prop="jpl" width="130"
                             :show-overflow-tooltip="true"/>
            <el-table-column label="排放量(t/a)" align="center" prop="pfl" width="130"
                             :show-overflow-tooltip="true"/>
          </el-table-column>
        </el-table>
      </el-card>
    </el-row>
    <el-row>
      <el-card style="height: 570px;">
        <div class="bt"><span><i class="el-icon-coin i"></i> 泄漏程度统计</span></div>
        <el-col :span="12">
          <el-card style="margin-top:0; margin-left:0;margin-bottom:0;height: 500px" shadow="hover">
            <plant-count-chart :statistics="statisticsList"></plant-count-chart>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card style="margin-top:0; margin-right:0;margin-bottom:0;height: 500px" shadow="hover">
            <pt-count-chart :queryParams="queryParams" v-if="show"></pt-count-chart>
          </el-card>
        </el-col>
      </el-card>
    </el-row>
    <el-row>
      <el-card style="height: 500px">
        <div class="bt"><span><i class="el-icon-coin i"></i> 排放信息统计</span></div>
        <pfl-of-year-chart v-if="show" :statistics="statisticsList"></pfl-of-year-chart>
      </el-card>
    </el-row>
    <el-row>
      <el-col :span="6">
        <el-card style="height: 525px">
          <el-table :data="statisticsList" border height="495" @row-click="selectRow">
            <el-table-column label="装置" align="center" prop="plantName"
                             :show-overflow-tooltip="true" width="80" />
            <el-table-column label="泄漏量(t/a)" align="center" prop="xll"
                             :show-overflow-tooltip="true"/>
            <el-table-column label="减排量(t/a)" align="center" prop="jpl"
                             :show-overflow-tooltip="true"/>
            <el-table-column label="排放量(t/a)" align="center" prop="pfl"
                             :show-overflow-tooltip="true"/>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="height: 525px">
          <pfl-pie-chart v-if="showPoint" :pointList="pointCountList" :plant-name="plantName"></pfl-pie-chart>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="height: 525px">
          <xll-pie-chart v-if="showPoint" :pointList="pointCountList" :plant-name="plantName"></xll-pie-chart>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="height: 525px">
          <jpl-pie-chart v-if="showPoint" :pointList="pointCountList" :plant-name="plantName"></jpl-pie-chart>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import PlantCountChart from "@/views/statistics/plantCountChart";
import {getAllPlantName} from "@/api/base/plant";
import {listStatistics, pflByPoint} from "@/api/statistics/statistics";
import PtCountChart from "@/views/statistics/ptCountChart";
import PflOfYearChart from "@/views/statistics/pflOfYearChart";
import PflPieChart from "@/views/statistics/pie/pflPieChart";
import XllPieChart from "@/views/statistics/pie/xllPieChart";
import JplPieChart from "@/views/statistics/pie/jplPieChart";

export default {
  name: "statistics",
  components: {JplPieChart, XllPieChart, PflPieChart, PflOfYearChart, PtCountChart, PlantCountChart},
  data() {
    return {
      plantName: '',
      show: false,
      showPoint: false,
      statisticsList: [],
      pointCountList: [],
      plantOperation: [],
      year: new Date(),
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        plantId: null,
        year: null,
        startTime: null,
        endTime: null,
        company: null,
      }
    }
  },
  created() {
    this.getList();
    getAllPlantName().then(response => {
      this.plantOperation = response.data;
    });
  },
  methods: {
    selectRow(row){
      this.plantName=row.plantName;
      this.showPoint=false;
      this.$nextTick(() => {
        this.showPoint=true
      })
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleQuery() {
      this.show = false;
      this.showPoint = false;
      this.getList();
    },
    getList() {
      this.queryParams.year = this.year.getFullYear();
      listStatistics(this.queryParams).then(response => {
        this.statisticsList = response.data;
        this.show = true;
      })
      pflByPoint(this.queryParams).then(response => {
        this.pointCountList = response.data;
        this.showPoint = true;
      })
    }
  }
}
</script>

<style scoped>
.el-card {
  margin: 5px;
}

.bt {
  margin-bottom: 10px;
  /* 火狐 */
  -moz-user-select: none;
  /* Safari 和 欧朋 */
  -webkit-user-select: none;
  /* IE10+ and Edge */
  -ms-user-select: none;
  /* Standard syntax 标准语法(谷歌) */
  user-select: none;
}

.i {
  color: #00afff;
}
</style>
