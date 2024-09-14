<template>
  <div class="app-container">
    <el-card class="search-box">
      <el-form
        :inline="true"
        :model="searchParams"
        @submit.native.prevent
      >
        <el-form-item label="课程表编号">
          <el-input v-model="searchParams.schoolTimetableId" placeholder="课程表编号" />
        </el-form-item>
        <el-form-item label="教学班级组编号">
          <el-input v-model="searchParams.teachClassGroupId" placeholder="教学班级组编号" />
        </el-form-item>
        <el-form-item label="课程编号">
          <el-input v-model="searchParams.courseId" placeholder="课程编号" />
        </el-form-item>
        <el-form-item label="课程名称">
          <el-input v-model="searchParams.courseName" placeholder="课程名称" />
        </el-form-item>
        <el-form-item label="教师编号">
          <el-input v-model="searchParams.teacherId" placeholder="教师编号" />
        </el-form-item>
        <el-form-item label="教师姓名">
          <el-input v-model="searchParams.teacherName" placeholder="教师姓名" />
        </el-form-item>
        <el-form-item label="周几">
          <el-input v-model="searchParams.weekDay" placeholder="周几" />
        </el-form-item>
        <el-form-item label="开始节">
          <el-input v-model="searchParams.daySectionStart" placeholder="开始节" />
        </el-form-item>
        <el-form-item label="结束节">
          <el-input v-model="searchParams.daySectionEnd" placeholder="结束节" />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            icon="el-icon-search"
            @click="fetchData"
          >查询</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="table-box">
      <div slot="header" class="clearfix">
        <div class="operate-box">
          <el-button
            type="success"
            icon="el-icon-circle-plus-outline"
            :disabled="autoArrange"
            @click="arrange"
          >自动排课</el-button>
        </div>
      </div>
      <el-table
        ref="teacherTable"
        v-loading="tableLoading"
        :data="tableData"
        element-loading-text="Loading"
        fit
        highlight-current-row
      >
        <el-table-column
          v-for="item in tableTitle"
          :key="item.id"
          :prop="item.id"
          :label="item.label"
          align="center"
        >
          <template slot-scope="scope">
            <span v-if="item.id === 'courseType'">{{ scope.row[item.id] === 0 ? '理论' : '实验' }}</span>
            <span v-else-if="item.id === 'status'">
              <el-tag
                disable-transitions
                :type="statusMap.get(scope.row[item.id]).status"
              >{{ statusMap.get(scope.row[item.id]).label }}
              </el-tag>
            </span>
            <span v-else>{{ scope.row[item.id] }}</span>
          </template>
        </el-table-column>
        <el-table-column class-name="status-col" label="操作" width="150" align="center">
          <template slot-scope="scope">
            <div style="display: flex; justify-content: center;align-items: center;">
              <el-button type="text" @click="viewSchedule(scope.row)">查看课表</el-button>
              <el-button v-if="scope.row.status === 0" type="text" @click="update(scope.row)">修改排课</el-button>
              <el-button v-if="scope.row.status === 2" type="text" @click="viewVolunteer(scope.row)">查看志愿</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <viewDialog ref="viewDialog" />
    <updateDialog ref="updateDialog" @create-success="fetchData" />
    <ChartDialog ref="chartDialog" />
  </div>
</template>

<script>
import { getScheduleList, autoArrangeSchedule } from '@/api/schedule-management'
import ViewDialog from './components/viewDialog.vue'
import ChartDialog from './components/chartDialog.vue'
import UpdateDialog from './components/updateDialog.vue'
export default {
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'gray',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  components: {
    ViewDialog,
    ChartDialog,
    UpdateDialog
  },
  data() {
    return {
      tableLoading: false,
      editVisible: false,
      deleteVisible: false,
      currentPage: 0,
      pageSizes: [10, 20, 40],
      pageSize: 10,
      total: 0,
      statusMap: new Map([
        [-1, {
          label: '未开始',
          status: 'primary'
        }],
        [0, {
          label: '失败',
          status: 'danger'
        }],
        [1, {
          label: '成功',
          status: 'success'
        }],
        [2, {
          label: '等待',
          status: 'info'
        }]
      ]),
      tableTitle: [
        {
          id: 'schoolTimetableId',
          label: '课程表编号'
        },
        {
          id: 'teachClassGroupId',
          label: '教学班级组编号'
        },
        {
          id: 'courseId',
          label: '课程编号'
        },
        {
          id: 'courseName',
          label: '课程名称'
        },
        {
          id: 'courseType',
          label: '课程类型'
        },
        {
          id: 'teacherId',
          label: '教师编号'
        },
        {
          id: 'teacherName',
          label: '教师姓名'
        },
        {
          id: 'weekDay',
          label: '周几'
        },
        {
          id: 'daySectionStart',
          label: '开始节'
        },
        {
          id: 'daySectionEnd',
          label: '结束节'
        },
        {
          id: 'status',
          label: '排课状态'
        }
      ],
      autoArrange: true,
      tableData: [],
      searchParams: {
        schoolTimetableId: '',
        teachClassGroupId: '',
        courseId: '',
        courseName: '',
        teacherId: '',
        teacherName: '',
        weekDay: '',
        daySectionStart: '',
        daySectionEnd: ''
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.tableLoading = true
      const {
        schoolTimetableId,
        teachClassGroupId,
        courseId,
        courseName,
        teacherId,
        teacherName,
        weekDay,
        daySectionStart,
        daySectionEnd
      } = this.searchParams
      const params = {
        schoolTimetableId,
        teachClassGroupId,
        courseId,
        courseName,
        teacherId,
        teacherName,
        weekDay,
        daySectionStart,
        daySectionEnd,
        queryVolunteerIs: true

      }
      getScheduleList(params).then(response => {
        const { data } = response
        let waitNum = 0
        data.forEach(item => {
          const { courseVolunteer } = item
          // const { status } = courseVolunteer
          item.status = -1

          if (courseVolunteer) {
            item.status = courseVolunteer.status !== null ? courseVolunteer.status : -1
          }
          if (item.status === 2) waitNum++
        })
        if (waitNum === data.length && waitNum !== 0) this.autoArrange = false
        this.tableData = [...data]
        this.tableLoading = false
      }).finally(() => {
        this.tableLoading = false
      })
    },
    arrange() {
      autoArrangeSchedule().then(res => {
        this.$message({
          message: '排课成功',
          type: 'success'
        })
      })
      this.fetchData()
    },
    // fetchSchedule() {
    //   this.viewLoading = true
    //   const params = {
    //     // teacherId: store.getters.userId,
    //     queryVolunteerIs: false
    //   }
    //   getScheduleList(params).then(res => {
    //     this.viewLoading = false
    //     const { data } = res
    //     this.scheduleData = data.map(item => {
    //       return {
    //         weekDay: item.weekDay,
    //         daySectionStart: item.daySectionStart,
    //         daySectionEnd: item.daySectionEnd
    //       }
    //     })
    //   })
    // },
    update(row) {
      this.$refs.updateDialog.showDialog(row)
    },
    viewVolunteer(row) {
      this.$refs.viewDialog.showDialog(row)
    },
    viewSchedule(id) {
      this.$refs.chartDialog.showDialog(id)
    },
    handleDelete() {
      // this.$refs.teacherTable.toggleRowSelection(row) {

      // }
    }
  }
}
</script>
    <style lang="scss">
    .app-container {
      .search-box {
        padding: 0 18px;
        margin-bottom: 10px;
      }
      .table-box {
        .clearfix {
          padding: 0 18px;
          .operate-box {
            float: left
          }
          .file-box {
            float: right;
            .import-button {
              margin-right: 20px;
            }
          }
        }
      }
    }
    </style>
