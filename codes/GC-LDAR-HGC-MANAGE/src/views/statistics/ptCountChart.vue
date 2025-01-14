<template>
  <div id="ptCountChart" :style="{height:height,width:width}"/>
</template>

<script>

import {xlcdByPoint} from "@/api/statistics/statistics";
import resize from "@/views/dashboard/mixins/resize";

export default {
  mixins: [resize],
  props: {
    queryParams: {
      type: Object,
      default() {
        return {};
      }
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
      dataList:[],
      option: {
        title: {
          text: '按密封点类型统计',
          textStyle: {color: '#666', fontSize: 14, fontWeight: 'normal'},
          padding: [5, 5, 5, 5],
        },
        legend: {
          orient: 'horizontal',
          top: 0,
          itemWidth: 30,
          itemHeight: 12,
          data: ['严重泄漏', '一般泄漏', '未泄漏']
        },
        grid: {left: 10, top: 50, bottom: 20, right: 20, containLabel: true},
        xAxis: {
          type: 'value',
          axisLine: {show: true, lineStyle: {color: '#ccc'}},
          axisLabel: {color: '#999'},
          splitLine: {lineStyle: {color: ['#CEEDFF'], type: [5, 8], dashOffset: 3}},
        },
        yAxis: {
          type: 'category',
          data: [],
          axisLine: {lineStyle: {color: '#ccc'}},
          axisTick: {length: 3},
          axisLabel: {color: '#999'},
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {type: 'shadow'},
          textStyle: {color: '#424242'},
          renderMode: 'html',
          className: 'tooltip',
          order: 'seriesDesc',
        },
        toolbox: {
          show: true,
          feature: {
            mark: {show: true},
            restore: {show: true},
            saveAsImage: {show: true}
          }
        },
        series: [
          {
            name: '严重泄漏',
            type: 'bar',
            stack: 'total', // ! 多条数据总计 => 堆叠
            barWidth: 30,
            color: '#f5804d',
            itemStyle: {borderRadius: [5, 5, 5, 5]},
            data: [],
          },
          {
            name: '一般泄漏',
            type: 'bar',
            stack: 'total', // ! 多条数据总计 => 堆叠
            barWidth: 30,
            color: '#FFC53D',
            itemStyle: {borderRadius: [5, 5, 5, 5]},
            data: [],
          },
          {
            name: '未泄漏',
            type: 'bar',
            stack: 'total', // ! 多条数据总计 => 堆叠
            barWidth: 30,
            color: '#8bd46e',
            itemStyle: {borderRadius: [5, 5, 5, 5]},
            data: [],
          },
        ],
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      xlcdByPoint(this.queryParams).then(response => {
        this.dataList=response.data;
        this.dataList.forEach(item => {
          this.option.series[0].data.push(item.yzxlCount == null ? 0 : item.yzxlCount);
          this.option.series[1].data.push(item.ybxlCount == null ? 0 : item.ybxlCount);
          this.option.series[2].data.push(item.wxlCount == null ? 0 : item.wxlCount);
          this.option.yAxis.data.push(item.pointType);
        })
        this.initChart();
      })
    })
  },
  methods: {
    initChart() {
      // 基于准备好的dom，初始化echarts实例
      this.chart = this.echarts.init(document.getElementById('ptCountChart'))
      this.chart.setOption(this.option)
    }
  }

}
</script>

<style scoped>

</style>
