a
<template>
  <div>
    <el-dialog

      class="chart-dialog"
      :title="teacherName + '教师的课程表'"
      :visible.sync="dialogVisible"
      width="60%"
    >
      <div v-if="chartData">
        <ScheduleChart :chart-data="chartData" :course-data="courseData" />
      </div>
      <div v-else v-loading="viewLoading" style="height: 450px; width: 100%;" />
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import ScheduleChart from './scheduleChart.vue'
import { getScheduleList } from '@/api/schedule-management'
export default {
  name: 'ClassDialog',
  components: {
    ScheduleChart
  },
  data() {
    return {
      dialogVisible: false,
      viewLoading: false,
      chartData: null,
      courseData: {},
      teacherName: ''
    }
  },
  watch: {
    dialogVisible(val) {
      if (!val) {
        this.chartData = null
        this.courseData = null
      }
    }
  },
  methods: {
    showDialog(row) {
      this.dialogVisible = true
      const {
        teacherName,
        teacherId
      } = row
      this.courseData = row
      this.teacherName = teacherName
      this.fetchSchedule(teacherId)
    },
    fetchSchedule(id) {
      this.viewLoading = true
      const params = {
        teacherId: id,
        queryVolunteerIs: false
      }
      getScheduleList(params).then(res => {
        this.viewLoading = false
        const { data } = res
        this.chartData = data.map(item => {
          return {
            courseName: item.courseName,
            weekDay: item.weekDay,
            daySectionStart: item.daySectionStart,
            daySectionEnd: item.daySectionEnd
          }
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.cascader {
  width: 250px !important;
}
</style>
