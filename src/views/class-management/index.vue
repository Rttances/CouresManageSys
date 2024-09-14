<template>
  <div class="app-container">
    <el-card class="search-box">
      <el-form
        :inline="true"
        :model="searchParams"
        @submit.native.prevent
      >
        <el-form-item label="班级编号">
          <el-input v-model="searchParams.classId" placeholder="课程编号" />
        </el-form-item>
        <el-form-item label="班级名称">
          <el-input v-model="searchParams.className" placeholder="班级名称" />
        </el-form-item>
        <el-form-item label="年级">
          <el-input v-model="searchParams.grade" placeholder="年级" />
        </el-form-item>
        <el-form-item label="专业名称">
          <el-input v-model="searchParams.majorName" placeholder="专业名称" />
        </el-form-item>
        <el-form-item label="方向名称">
          <el-input v-model="searchParams.directionName" placeholder="方向名称" />
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
          <el-button
            type="primary"
            icon="el-icon-circle-plus-outline"
            @click="merge"
          >合并教学班</el-button>
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
        ref="teacherTable"
        v-loading="tableLoading"
        :data="tableData"
        element-loading-text="Loading"
        fit
        highlight-current-row
        row-key="classId"
        @selection-change="handleSelectChange"
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
        <el-table-column class-name="status-col" label="操作" width="150" align="center">
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
    <MergeDialog ref="mergeDialog" @create-success="fetchData" />
    <ClassDialog ref="classDialog" @create-success="fetchData" />
    <UploadDialog ref="uploadDialog" @create-success="fetchData" />
  </div>
</template>

<script>
import { getClassList, getDemoPath } from '@/api/class-management'
import ClassDialog from './components/classDialog.vue'
import UploadDialog from './components/uploadDialog.vue'
import MergeDialog from './components/mergeDialog.vue'
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
    ClassDialog,
    UploadDialog,
    MergeDialog
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
          id: 'classId',
          label: '班级编号'
        },
        {
          id: 'className',
          label: '班级名称'
        },
        {
          id: 'grade',
          label: '年级'
        },
        {
          id: 'majorName',
          label: '专业名称'
        },
        {
          id: 'directionName',
          label: '方向名称'
        },
        {
          id: 'studentNumber',
          label: '学生人数'
        }
      ],
      tableData: [],
      selectionTable: [],
      searchParams: {
        classId: '',
        className: '',
        grade: '',
        majorName: '',
        directionName: ''
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
        classId,
        className,
        grade,
        majorName,
        directionName,
        studentNumber
      } = this.searchParams
      const params = {
        entity: {
          classId,
          className,
          grade,
          majorName,
          directionName,
          studentNumber
        },
        pageNumber: this.currentPage,
        pageSize: this.pageSize,
        orderBy: '',
        orderType: 'ASC'
        // isAll: false
      }
      getClassList(params).then(response => {
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
      this.$refs.classDialog.showDialog()
    },
    edit(row) {
      this.$refs.classDialog.showDialog(row)
    },
    handleSelectChange(selection) {
      this.selectionTable = selection
    },
    merge() {
      if (this.selectionTable.length < 2) {
        this.$message({
          message: '请选择至少两个需要合并的班级',
          type: 'warning'
        })
      } else {
        this.$refs.mergeDialog.showDialog(this.selectionTable)
      }
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
          window.open('http://localhost:10010/file/download?filename=' + data)
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
    .el-table__expanded-cell {
      padding: 0 0 0 50px !important;
    }
  }
  </style>
