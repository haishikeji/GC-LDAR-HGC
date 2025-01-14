<template>
  <el-row :gutter="40" class="panel-group">
    <el-col :xs="16" :sm="16" :lg="8" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-people">
          <svg-icon icon-class="dashboard" class-name="card-panel-icon"/>
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            初始排放量
          </div>
          <count-to :start-val="0" :end-val="pfl" :duration="2600" :decimals="4" class="card-panel-num"/>
          吨/年
        </div>
      </div>
    </el-col>
    <el-col :xs="16" :sm="16" :lg="8" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-message">
          <svg-icon icon-class="dashboard" class-name="card-panel-icon"/>
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            减排量
          </div>
          <count-to :start-val="0" :end-val="jpl" :duration="3000" :decimals="4" class="card-panel-num"/>
          吨/年
        </div>
      </div>
    </el-col>
    <el-col :xs="16" :sm="18" :lg="8" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-money">
          <svg-icon icon-class="dashboard" class-name="card-panel-icon"/>
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            最终排放量
          </div>
          <count-to :start-val="0" autopa :end-val="sjpfl" :duration="3200" :decimals="4" class="card-panel-num"/>
          吨/年
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import CountTo from 'vue-count-to'
import {listStatistics, pflByPoint} from "@/api/statistics/statistics";

export default {
  props: {
    statisticsList: {
      type: Array,
      default() {
        return [];
      }
    },
  },
  components: {
    CountTo
  },
  data() {
    return {
      pfl: 0,
      jpl: 0,
      sjpfl: 0,
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.statisticsList.forEach(item => {
        if (item.plantName == '合计') {
          this.pfl = parseFloat(item.pfl);
          this.jpl = parseFloat(item.jpl);
          this.sjpfl = parseFloat(item.sjpfl);
        }
      })
    })
  },
  watch: {
    statisticsList: {
      deep: true,
      immediate: true, // 监听到后，立即执行 handler方法
      handler: function(newVal, oldVal) {
        this.statisticsList = newVal
        this.statisticsList.forEach(item => {
          if (item.plantName == '合计') {
            this.pfl = parseFloat(item.pfl);
            this.jpl = parseFloat(item.jpl);
            this.sjpfl = parseFloat(item.sjpfl);
          }
        })
      },
    }
  },
  methods: {
    handleSetLineChartData(type) {
      this.$emit('handleSetLineChartData', type)
    },
  }
}
</script>

<style lang="scss" scoped>
.panel-group {
  margin-top: 18px;

  .card-panel-col {
    margin-bottom: 32px;
  }

  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);

    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-people {
        background: #40c9c6;
      }

      .icon-message {
        background: #36a3f7;
      }

      .icon-money {
        background: #f4516c;
      }

      .icon-shopping {
        background: #34bfa3
      }
    }

    .icon-people {
      color: #40c9c6;
    }

    .icon-message {
      color: #36a3f7;
    }

    .icon-money {
      color: #f4516c;
    }

    .icon-shopping {
      color: #34bfa3
    }

    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px;
      margin-left: 0px;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }

      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}

@media (max-width: 550px) {
  .card-panel-description {
    display: none;
  }

  .card-panel-icon-wrapper {
    float: none !important;
    width: 100%;
    height: 100%;
    margin: 0 !important;

    .svg-icon {
      display: block;
      margin: 14px auto !important;
      float: none !important;
    }
  }
}
</style>
