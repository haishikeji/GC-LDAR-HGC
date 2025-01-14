<template>
  <div class="dashboard-editor-container">

    <panel-group :statisticsList="statisticsList"/>

    <el-row  style="background:#fff;margin-bottom:32px;">
      <el-card style="height: 570px">
        <div class="bt"><span><i class="el-icon-coin i"></i> 泄漏程度统计</span></div>
        <el-col :span="12">
          <el-card style="margin:0 5px 0 0;height: 500px" shadow="hover">
            <plant-count-chart :statistics="xlcdList"></plant-count-chart>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card style="margin: 0 0 0 5px;height: 500px " shadow="hover">
            <pt-count-chart :queryParams="queryParams"></pt-count-chart>
          </el-card>
        </el-col>
      </el-card>
    </el-row>

    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <pfl-pie-chart v-if="showPoint" :pointList="pointCountList" :plant-name="''"></pfl-pie-chart>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <xll-pie-chart v-if="showPoint" :pointList="pointCountList" :plant-name="''"></xll-pie-chart>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <jpl-pie-chart v-if="showPoint" :pointList="pointCountList" :plant-name="''"></jpl-pie-chart>
        </div>
      </el-col>
    </el-row>


  </div>
</template>

<script>
import PanelGroup from './dashboard/PanelGroup'
import LineChart from './dashboard/LineChart'
import RaddarChart from './dashboard/RaddarChart'
import PieChart from './dashboard/PieChart'
import BarChart from './dashboard/BarChart'
import {countXlcd, listStatistics, pflByPoint} from "@/api/statistics/statistics";
import PflPieChart from "@/views/statistics/pie/pflPieChart";
import XllPieChart from "@/views/statistics/pie/xllPieChart";
import JplPieChart from "@/views/statistics/pie/jplPieChart";
import PlantCountChart from "@/views/statistics/plantCountChart";
import PtCountChart from "@/views/statistics/ptCountChart";

const lineChartData = {
  newVisitis: {
    expectedData: [100, 120, 161, 134, 105, 160, 165],
    actualData: [120, 82, 91, 154, 162, 140, 145]
  },
  messages: {
    expectedData: [200, 192, 120, 144, 160, 130, 140],
    actualData: [180, 160, 151, 106, 145, 150, 130]
  },
  purchases: {
    expectedData: [80, 100, 121, 104, 105, 90, 100],
    actualData: [120, 90, 100, 138, 142, 130, 130]
  },
  shoppings: {
    expectedData: [130, 140, 141, 142, 145, 150, 160],
    actualData: [120, 82, 91, 154, 162, 140, 130]
  }
}

export default {
  name: 'Index',
  components: {
    PtCountChart,
    PlantCountChart,
    JplPieChart,
    XllPieChart,
    PflPieChart,
    PanelGroup,
    LineChart,
    RaddarChart,
    PieChart,
    BarChart
  },
  data() {
    return {
      lineChartData: lineChartData.newVisitis,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        plantId: null,
        year: null,
        startTime: null,
        endTime: null,
        company: null,
      },
      show: false,
      showPoint: false,
      statisticsList: [],
      xlcdList: [],
      pointCountList: [],
    }
  },
  created() {
    this.getList();
  },
  methods: {
    handleSetLineChartData(type) {
      this.lineChartData = lineChartData[type]
    },
    getList() {
      this.queryParams.year = new Date().getFullYear();
      listStatistics(this.queryParams).then(response => {
        this.statisticsList = response.data;
        this.show = true;
      })
      pflByPoint(this.queryParams).then(response => {
        this.pointCountList = response.data;
        this.showPoint = true;
      })
      countXlcd(this.queryParams).then(response => {
        this.xlcdList = response.data;
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 10px 32px 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
