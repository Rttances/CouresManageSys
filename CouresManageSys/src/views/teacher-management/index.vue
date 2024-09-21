<template>
  <div class="app-container">
    <el-card class="search-box">
      <el-form
        :inline="true"
        :model="searchParams"
        @submit.native.prevent
      >
        <el-form-item label="教师编号">
          <el-input v-model="searchParams.teacherId" placeholder="教师编号" />
        </el-form-item>
        <el-form-item label="教师姓名">
          <el-input v-model="searchParams.teacherName" placeholder="教师姓名" />
        </el-form-item>
        <el-form-item label="职称">
          <el-select v-model="searchParams.teacherLevel" style="width: 120px" placeholder="职称">
            <el-option
              v-for="item in levelOptions"
              :key="item.value"
              :value="item.value"
              :label="item.label"
            />
          </el-select>
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
            class="import-button"
            icon="el-icon-download"
            @click="handlerDownloadExcelDemo"
          >
            下载模版
          </el-button>
        </div>
      </div>
      <el-table
        ref="teacherTable"
        v-loading="tableLoading"
        :data="tableData"
        element-loading-text="Loading"
        fit
        highlight-current-row
        row-key="teacherId"
      >
        <el-table-column type="selection" :reserve-selection="true" width="55" />
        <el-table-column
          v-for="item in tableTitle"
          :key="item.id"
          :prop="item.id"
          :label="item.label"
          align="center"
        >
          <template slot-scope="scope">
            <span>{{ scope.row[item.id] }}</span>
          </template>
        </el-table-column>
        <el-table-column class-name="status-col" label="操作" width="110" align="center">
          <template slot-scope="scope">
            <el-button type="text" @click="edit(scope.row)">编辑</el-button>
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
    <TeacherDialog ref="teacherDialog" @create-success="fetchData" />
    <UploadDialog ref="uploadDialog" @create-success="fetchData" />
  </div>
</template>

<script>
import { getTeacherList, getDemoPath } from '@/api/teacher-management'
import TeacherDialog from './components/teacherDialog.vue'
import UploadDialog from './components/uploadDialog.vue'
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
    TeacherDialog,
    UploadDialog
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
          id: 'teacherId',
          label: '教师编号'
        },
        {
          id: 'teacherName',
          label: '教师名称'
        },
        {
          id: 'teacherLevel',
          label: '职称'
        }
      ],
      tableData: [],
      levelOptions: [
        {
          value: '',
          label: '全部'
        },
        {
          value: '讲师',
          label: '讲师'
        },
        {
          value: '高级讲师',
          label: '高级讲师'
        },
        {
          value: '副教授',
          label: '副教授'
        },
        {
          value: '教授',
          label: '教授'
        }
      ],
      searchParams: {
        teacherName: '',
        teacherId: '',
        teacherLevel: ''
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
        teacherName,
        teacherId,
        teacherLevel
      } = this.searchParams
      const params = {
        entity: {
          teacherId,
          teacherName,
          teacherLevel
        },
        pageNumber: this.currentPage,
        pageSize: this.pageSize,
        orderBy: '',
        orderType: 'ASC',
        isAll: false
      }
      getTeacherList(params).then(response => {
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
      this.$refs.teacherDialog.showDialog()
    },
    edit(row) {
      this.$refs.teacherDialog.showDialog(row)
    },
    upload() {
      this.$refs.uploadDialog.showDialog()
    },
    handleDelete() {
      // this.$refs.teacherTable.toggleRowSelection(row) {

      // }
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
</style>
