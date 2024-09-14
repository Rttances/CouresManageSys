a
<template>
  <div>
    <el-dialog
      class="chart-dialog"
      title="课程表"
      :visible.sync="dialogVisible"
      width="60%"
    >
      <div>
        <ScheduleChart :chart-data="chartData" />
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>

      </span>
    </el-dialog>
  </div>
</template>

<script>
import ScheduleChart from './scheduleChart.vue'
export default {
  name: 'ClassDialog',
  components: {
    ScheduleChart
  },
  data() {
    return {
      dialogVisible: false,
      chartData: ''
    }
  },
  methods: {
    showDialog(data) {
      this.dialogVisible = true
      this.chartData = data
    }
  }
}
</script>

<style lang="scss" scoped>
.cascader {
  width: 250px !important;
}
</style>
