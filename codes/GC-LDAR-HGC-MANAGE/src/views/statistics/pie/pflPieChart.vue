<template>
  <div id="pflPieChart" :style="{height:height,width:width}"/>
</template>

<script>
import resize from "@/views/dashboard/mixins/resize";

export default {
  name: "pflPieChart",
  mixins: [resize],
  props: {
    pointList: {
      type: Array,
      default() {
        return [];
      }
    },
    plantName: {
      type: String,
      default: ''
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '490px'
    },
  },
  data() {
    return {
      chart: null,
      option: {
        title: {
          text: '排放量',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          orient: 'horizontal',
          x: 'left',
          y:'bottom',
          data: []
        },
        toolbox: {
          show: true,
          feature: {
            mark: {show: true},
            magicType: {
              show: true,
              type: ['funnel']
            },
            restore: {show: true},
            saveAsImage: {show: true}
          }
        },
        series: [
          {
            label: {
              formatter: '{b}: ({d}%)'
            },
            name: '排放量',
            type: 'pie',
            radius: ['40%', '60%'],
            selectedMode: 'single',
            data: [],
            emphasis: {
              label: {
                show: true,
                fontSize: '13',
                fontWeight: 'bold'
              }
            },
          }
        ]
      },
    }
  },
  mounted() {
    this.$nextTick(() => {
      for (let i = 0; i < this.pointList.length; i++) {
        let flag = true;
        let j = 0
        for (j; j < this.option.series[0].data.length; j++) {
          if (this.option.series[0].data[j].name == this.pointList[i].pointType) {
            flag = false;
            break;
          }
        }
        if ((this.plantName != '合计' && this.plantName == this.pointList[i].plantName)
          || this.plantName == '合计' || this.plantName == '') {
          if (flag) {
            this.option.legend.data.push(this.pointList[i].pointType)
            this.option.series[0].data.push({value: this.pointList[i].pfl, name: this.pointList[i].pointType})
          } else {
            this.option.series[0].data[j] = {
              value: parseFloat(this.option.series[0].data[j].value) + parseFloat(this.pointList[i].pfl),
              name: this.pointList[i].pointType
            }
          }
        }
      }
      this.initChart();
    })
  },
  methods: {
    initChart() {
      // 基于准备好的dom，初始化echarts实例
      this.chart = this.echarts.init(document.getElementById('pflPieChart'))
      this.chart.setOption(this.option)
    },
  }
}
</script>

<style scoped>

</style>
