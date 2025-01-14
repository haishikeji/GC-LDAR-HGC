<template>
  <div id="pflOfYearChart" :style="{height:height,width:width}"/>
</template>

<script>

export default {
  props: {
    statistics: {
      type: Array,
      default() {
        return [];
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
      option: {
        title: {
          text: '年度总排放量(t/a)',
          textStyle: {color: '#00c4ff', fontSize: 18, fontWeight: 'normal',},
          left: 'center'
        },
        grid: {left: 10, top: 50, bottom: 60, right: 20, containLabel: true},
        xAxis: {
          type: 'category',
          data: ['初始排放量', '泄漏量', '减排量', '最终排放量'],
          axisLine: {lineStyle: {color: '#ccc'}},
          axisTick: {length: 3},
          axisLabel: {color: '#999'},
        },
        yAxis: {
          type: 'value',
          axisLine: {show: true, lineStyle: {color: '#ccc'}},
          axisLabel: {color: '#999'},
          splitLine: {lineStyle: {color: ['#CEEDFF'], type: [5, 8], dashOffset: 3}},
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
        series:
          {
            name: '',
            type: 'bar',
            barWidth: 230,
            itemStyle: {
              normal: {
                borderRadius: [5, 5, 0, 0],
                show: true,
                color: function (params) {
                  let colorList = ['#FF6633', '#FFCC00', '#00CC33', '#FF6633'];
                  const num = colorList.length;
                  return colorList[params.dataIndex % num];
                },
              }
            },
            label: { //显示数值的地方
              normal: {
                position: 'top',
                show: true,
                formatter: function (res) {
                  var label = res.name + res.value;
                  return label
                },
                textStyle: { //数值样式
                  color:'#666',
                }
              },
            },

            data: [],
          },

      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.statistics.forEach(item => {
        if (item.plantName == '合计') {
          this.option.series.data.push(parseFloat(item.pfl));
          this.option.series.data.push(parseFloat(item.xll));
          this.option.series.data.push(parseFloat(item.jpl));
          this.option.series.data.push(parseFloat(item.sjpfl));
        }
      })
      this.initChart();
    })
  },
  methods: {
    initChart() {
      // 基于准备好的dom，初始化echarts实例
      this.chart = this.echarts.init(document.getElementById('pflOfYearChart'))
      this.chart.setOption(this.option)
    }
  }

}
</script>

<style scoped>

</style>
