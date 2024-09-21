<template>
  <div class="app-container">
    <el-card class="search-box">
      <el-form
        :inline="true"
        :model="searchParams"
        @submit.native.prevent
      >
        <el-form-item label="教学组编号">
          <el-input v-model="searchParams.teacherClassGroupId" placeholder="教学组编号" />
        </el-form-item>
        <el-form-item label="课程编号">
          <el-input v-model="searchParams.courseId" placeholder="课程编号" />
        </el-form-item>
        <el-form-item label="课程名称">
          <el-input v-model="searchParams.courseName" placeholder="课程名称" />
        </el-form-item>
        <el-form-item label="课程类型">
          <el-select v-model="searchParams.courseType" placeholder="请选择课程类型">
            <el-option
              v-for="item in courseType"
              :key="item.value"
              :value="item.value"
              :label="item.label"
            />
          </el-select>
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
            :loading="viewLoading"
            @click="viewSchedule"
          >查看课程表</el-button>
        </div>
      </div>
      <el-table
        ref="teacherTable"
        v-loading="tableLoading"
        :data="tableData"
        element-loading-text="Loading"
        fit
        highlight-current-row
        :row-class-name="getRowClass"
      >
        <el-table-column type="expand" style="width: 20px">
          <template slot-scope="props">
            <el-table
              ref="orderTable"
              v-loading="tableLoading"
              :data="props.row.courseVolunteer.volunteerTime"
              element-loading-text="Loading"
              fit
              highlight-current-row
              :header-row-style="headerRowStyle"
              :cell-style="cellStyle"
            >
              <el-table-column
                v-for="item in orderTitle"
                :key="item.id"
                :prop="item.id"
                :label="item.label"
                align="center"
              >
                <template slot-scope="prop">
                  <span v-if="item.id === 'order'">{{ orderMap.get(prop.row[item.id]) }}</span>
                  <span v-else-if="item.id === 'weekDay'">{{ dayMap.get(prop.row[item.id]) }}</span>
                  <span v-else>{{ courseMap.get(prop.row[item.id]) }}</span>
                </template>
              </el-table-column>
            </el-table>
          </template>
        </el-table-column>
        <el-table-column
          v-for="item in tableTitle"
          :key="item.id"
          :prop="item.id"
          :label="item.label"
          align="center"
        >
          <template slot-scope="prop">
            <span v-if="item.id === 'teachClassGroupName'">{{ prop.row['teachClassGroupVO'].groupName }}</span>
            <span v-if="item.id !== 'courseType'">{{ prop.row[item.id] }}</span>
            <span v-else>{{ prop.row[item.id] === 0 ? '理论' : '实验' }}</span>
          </template>
        </el-table-column>
        <el-table-column class-name="status-col" label="操作" width="150" align="center">
          <template slot-scope="scopes">
            <el-button v-if="scopes.row.status === -1" type="text" @click="arrange(scopes.row)">排课</el-button>
            <el-button v-if="scopes.row.status === 2" type="text" @click="edit(scopes.row, isEdit)">修改志愿</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- <el-pagination
        :current-page="currentPage"
        :page-sizes="pageSizes"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      /> -->
    </el-card>
    <ArrangeDialog ref="arrangeDialog" @create-success="fetchData" />
    <ChartDialog ref="chartDialog" />
  </div>
</template>

<script>
import { getScheduleList } from '@/api/schedule-management'
import ArrangeDialog from './components/arrangeDialog.vue'
import ChartDialog from './components/chartDialog.vue'
import store from '@/store'

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
    ArrangeDialog,
    ChartDialog
  },
  data() {
    return {
      tableLoading: false,
      editVisible: false,
      deleteVisible: false,
      // currentPage: 0,
      // pageSizes: [10, 20, 40],
      // pageSize: 10,
      // total: 0,

      orderMap: new Map([
        [0, '第一志愿'],
        [1, '第二志愿'],
        [2, '第三志愿']
      ]),
      dayMap: new Map([
        [1, '星期一'],
        [2, '星期二'],
        [3, '星期三'],
        [4, '星期四'],
        [5, '星期五'],
        [6, '星期六'],
        [7, '星期日']
      ]),

      courseMap: new Map([
        [1, '第一节'],
        [2, '第二节'],
        [3, '第三节'],
        [4, '第四节'],
        [5, '第五节'],
        [6, '第六节'],
        [7, '第七节']
      ]),
      courseType: [
        { label: '全部', value: '' },
        { label: '课程', value: 0 },
        { label: '实验', value: 1 }
      ],
      viewLoading: false,
      orderTitle: [
        {
          id: 'order',
          label: '志愿顺序'
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
        }
      ],
      tableTitle: [
        {
          id: 'teachClassGroupId',
          label: '教学组班级编号'
        },
        {
          id: 'teachClassGroupName',
          label: '教学组名称'
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
        }
      ],
      tableData: [],
      scheduleData: [],
      searchParams: {
        teachClassGroupId: '',
        courseId: '',
        courseName: '',
        courseType: '',
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
      this.viewLoading = true
      this.tableLoading = true
      const {
        teachClassGroupId,
        courseId,
        courseName,
        courseType,
        weekDay,
        daySectionStart,
        daySectionEnd
      } = this.searchParams
      const params = {
        teacherId: store.getters.userId,
        queryVolunteerIs: true,
        teachClassGroupId,
        courseId,
        courseName,
        courseType,
        weekDay,
        daySectionStart,
        daySectionEnd
      }
      getScheduleList(params).then(res => {
        const { data } = res
        this.tableData = data
        this.scheduleData = data.map(item => {
          const { courseVolunteer } = item
          // const { status } = courseVolunteer
          item.status = -1
          if (courseVolunteer) {
            item.status = courseVolunteer.status !== null ? courseVolunteer.status : -1
            // if (courseVolunteer.status === 2) this.autoArrange = false
          }
          return {
            courseName: item.courseName,
            weekDay: item.weekDay,
            daySectionStart: item.daySectionStart,
            daySectionEnd: item.daySectionEnd
          }
        })
        this.tableLoading = false
        this.viewLoading = false
      }).finally(() => {
        this.tableLoading = false
        this.viewLoading = false
      })
    },
    getRowClass({ row, rowIndex }) {
      if (row.status !== 2) return 'row-expand-cover'
    },
    cellStyle() {
      return {
        color: 'blue'
      }
    },
    headerRowStyle() {
      return {
        color: 'blue'
      }
    },
    // handleSizeChange(val) {
    //   this.pageSize = val
    //   this.currentPage = 0
    // },
    // handleCurrentChange(val) {
    //   this.currentPage = val
    //   this.fetchData()
    // },
    arrange(row) {
      this.$refs.arrangeDialog.showDialog(row)
    },
    viewSchedule() {
      this.$refs.chartDialog.showDialog(this.scheduleData)
    },
    edit(row) {
      const edit = true
      this.$refs.arrangeDialog.showDialog(row, edit)
    },
    handleDelete() {

    }
    // cellStyle() {
    //   return {
    //     color: 'blue'
    //   }
    // },
    // headerRowStyle() {
    //   return {
    //     color: 'blue'
    //   }
    // }
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
    //   .file-box {
    //     float: right;
    //     .import-button {
    //       margin-right: 20px;
    //     }
    //   }
    }
  }
}
.el-table {
  .el-table__expanded-cell {
    padding: 0 0 0 50px !important;
  }
  .el-table__expand-icon {
    color: #4b9ae9;
  }
  .row-expand-cover .cell .el-table__expand-icon {
    display: none;
  }
}

</style>
