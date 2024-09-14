<template>
  <div class="app-container">
    <el-card class="search-box">
      <el-form
        :inline="true"
        :model="searchParams"
        @submit.native.prevent
      >
        <el-form-item label="课程编号">
          <el-input v-model="searchParams.courseId" placeholder="课程编号" />
        </el-form-item>
        <el-form-item label="课程名称">
          <el-input v-model="searchParams.courseName" placeholder="课程名称" />
        </el-form-item>
        <el-form-item label="学分">
          <el-input v-model="searchParams.score" placeholder="学分" />
        </el-form-item>
        <el-form-item label="总课时">
          <el-input v-model="searchParams.totalClassHour" placeholder="总课时" />
        </el-form-item>
        <el-form-item label="理论课时">
          <el-input v-model="searchParams.theoreticalClassHour" placeholder="理论课时" />
        </el-form-item>
        <el-form-item label="实验课时">
          <el-input v-model="searchParams.experimentalClassHour" placeholder="实验课时" />
        </el-form-item>
        <el-form-item label="周理论课时">
          <el-input v-model="searchParams.weekTheoreticalClassHour" placeholder="周理论课时" />
        </el-form-item>
        <el-form-item label="周实验课时">
          <el-input v-model="searchParams.weekExperimentalClassHour" placeholder="周实验课时" />
        </el-form-item>
        <el-form-item label="起始周">
          <el-input v-model="searchParams.startWeek" placeholder="起始周" />
        </el-form-item>
        <el-form-item label="结束周">
          <el-input v-model="searchParams.endWeek" placeholder="结束周" />
        </el-form-item>
        <el-form-item label="归属系">
          <el-input v-model="searchParams.belongSystem" placeholder="归属系" />
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
            @click="add"
          >新增</el-button>
          <el-button
            type="danger"
            icon="el-icon-delete"
            @click="handleDelete"
          >删除</el-button>
        </div>
        <div class="file-box">
          <el-button
            class="import-button"
            icon="el-icon-upload2"
            type="primary"
            @click="upload"
          >
            导入
          </el-button>
          <el-button
            class="export-button"
            icon="el-icon-download"
            @click="handlerDownloadExcelDemo"
          >下载模版</el-button>
        </div>
      </div>
      <el-table
        ref="courseTable"
        v-loading="tableLoading"
        :data="tableData"
        element-loading-text="Loading"
        fit
        row-key="courseId"
        highlight-current-row
        :row-class-name="getRowClass"
      >
        <el-table-column type="selection" :reserve-selection="true" width="25" />
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-table
              ref="schoolTimetableTable"
              v-loading="tableLoading"
              :data="props.row.schoolTimetables"
              element-loading-text="Loading"
              fit
              highlight-current-row
              :header-row-style="headerRowStyle"
              :cell-style="cellStyle"
            >
              <el-table-column
                v-for="item in teacherTableTitle"
                :key="item.id"
                :prop="item.id"
                :label="item.label"
                align="center"
              >
                <template slot-scope="prop">
                  <span v-if="item.id === 'teachClassGroupName'">
                    {{ prop.row['teachClassGroupVO'].groupName }}
                  </span>
                  <span v-if="item.id !== 'courseType'">{{ prop.row[item.id] }}</span>
                  <span v-else>{{ prop.row[item.id] === 0 ? '课程' : '实验' }}</span>
                </template>
              </el-table-column>
              <el-table-column class-name="status-col" label="操作" width="150" align="center">
                <!-- <template slot-scope="prop"> -->
                <!-- <el-button type="text" @click="editSchedule(prop.row)">编辑</el-button> -->
                <el-button type="text" @click="deleteVisible = true">删除</el-button>
                <!-- </template> -->
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
          <template slot-scope="scope">
            <span v-if="item.id !== 'teacherNameSet'">{{ scope.row[item.id] }}</span>
            <span v-else>
              <el-tag
                v-for="name in scope.row[item.id]"
                :key="name"
                style="margin-left: 5px;margin-bottom: 5px;"
                type="success"
                disable-transitions
              >{{ name }}
              </el-tag>
            </span>
          </template>
        </el-table-column>
        <el-table-column class-name="status-col" label="操作" width="150" align="center">
          <template slot-scope="scope">
            <el-button type="text" @click="edit(scope.row)">编辑</el-button>
            <el-button type="text" @click="bind(scope.row)">绑定</el-button>
            <el-button type="text" @click="deleteVisible = true">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        :current-page="currentPage"
        :page-sizes="pageSizes"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>
    <CourseDialog ref="courseDialog" @create-success="fetchData" />
    <UploadDialog ref="uploadDialog" @create-success="fetchData" />
    <BindDialog ref="bindDialog" @create-success="fetchData" />
  </div>
</template>

<script>
import { getCourseList, getDemoPath } from '@/api/course-management'
import CourseDialog from './components/courseDialog.vue'
import UploadDialog from './components/uploadDialog.vue'
import BindDialog from './components/bindDialog.vue'
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
    CourseDialog,
    UploadDialog,
    BindDialog
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
      tableTitle: [
        {
          id: 'courseId',
          label: '课程编号'
        },
        {
          id: 'courseName',
          label: '课程名称'
        },
        {
          id: 'score',
          label: '学分'
        },
        {
          id: 'totalClassHour',
          label: '总课时'
        },
        {
          id: 'theoreticalClassHour',
          label: '理论课时'
        },
        {
          id: 'experimentalClassHour',
          label: '实验课时'
        },
        {
          id: 'weekTheoreticalClassHour',
          label: '周理论课时'
        },
        {
          id: 'weekExperimentalClassHour',
          label: '周实验课时'
        },
        {
          id: 'startWeek',
          label: '起始周'
        },
        {
          id: 'endWeek',
          label: '结束周'
        },
        {
          id: 'belongSystem',
          label: '归属系'
        }
      ],
      teacherTableTitle: [
        {
          id: 'teacherId',
          label: '教师编号'
        },
        {
          id: 'teacherName',
          label: '教师名称'
        },
        {
          id: 'teachClassGroupId',
          label: '教学班级组编号'
        },
        {
          id: 'teachClassGroupName',
          label: '教学班名称'
        },
        {
          id: 'courseType',
          label: '课程类型'
        }
      ],
      tableData: [],
      searchParams: {
        courseId: '',
        score: '',
        totalClassHour: '',
        theoreticalClassHour: '',
        experimentalClassHour: '',
        weekTheoreticalClassHour: '',
        weekExperimentalClassHour: '',
        startWeek: '',
        endWeek: '',
        belongSystem: '',
        courseName: ''
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
        courseId,
        score,
        totalClassHour,
        theoreticalClassHour,
        experimentalClassHour,
        weekTheoreticalClassHour,
        weekExperimentalClassHour,
        startWeek,
        endWeek,
        belongSystem,
        courseName
      } = this.searchParams
      const params = {
        entity: {
          courseId,
          score,
          totalClassHour,
          theoreticalClassHour,
          experimentalClassHour,
          weekTheoreticalClassHour,
          weekExperimentalClassHour,
          startWeek,
          endWeek,
          belongSystem,
          courseName
        },
        pageNumber: this.currentPage,
        pageSize: this.pageSize,
        orderBy: '',
        orderType: 'ASC',
        isAll: false
      }
      getCourseList(params).then(response => {
        const { data } = response
        const { records, pageNumber, total } = data
        this.tableData = [...records]
        this.currentPage = pageNumber
        this.total = total
        this.tableLoading = false
      }).finally(() => {
        this.tableLoading = false
      })
    },
    getRowClass({ row, rowIndex }) {
      console.log(row, rowIndex)
      if (row.schoolTimetables.length === 0) return 'row-expand-cover'
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.currentPage = 0
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.fetchData()
    },
    add() {
      this.$refs.courseDialog.showDialog()
    },
    edit(row) {
      this.$refs.courseDialog.showDialog(row)
    },
    upload() {
      this.$refs.uploadDialog.showDialog()
    },
    editSchedule(row) {
      this.$refs.bindDialog.showDialog(row)
    },
    bind(row) {
      this.$refs.bindDialog.showDialog(row.courseId)
    },
    handleDelete() {
      // this.$refs.teacherTable.toggleRowSelection(row) {

      // }
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
    handlerDownloadExcelDemo() {
      getDemoPath()
        .then(res => {
          const { data } = res
          const { filename } = data
          window.open('http://localhost:10010/file/download?filename=' + filename)
        }).then(res => {
          this.$message({
            message: '下载成功',
            type: 'success'
          })
        })
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
  .el-table {
    .row-expand-cover .cell .el-table__expand-icon {
    display: none;
  }
  .el-table__expanded-cell {
    padding: 0 0 0 50px !important;
  }
}
  </style>
