<template>
  <div>
    <el-dialog
      class="parsed-dialog"
      title="班级信息解析"
      :visible.sync="dialogVisible"
      width="80%"
    >
      <div class="parseTip">班级信息共解析{{ total }}条，其中成功解析{{ successParse }}条，解析失败{{ failedParse }}条</div>
      <el-table
        ref="parsedtable"
        v-loading="tableLoading"
        height="430"
        :data="tableData"
        element-loading-text="Loading"
        fit
        highlight-current-row
        :row-style="handleRowStyle"
        @selection-change="handleSelectChange"
      >
        <el-table-column
          type="selection"
          width="55"
          :selectable="handleSelectable"
        />
        <el-table-column
          v-for="item in tableTitle"
          :key="item.id"
          :prop="item.id"
          :label="item.label"
          :disabled="item"
          align="center"
        >
          <template slot-scope="scope">
            <span v-if="item.id === 'successImport'">{{ scope.row[item.id] ? '是': '否' }}</span>
            <span v-else>{{ scope.row[item.id] }}</span>
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
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button
          v-loading="confirmLoading"
          type="primary"
          @click="handleConfirm"
        >添 加</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getParsedList,
  createBatchInfo
} from '@/api/class-management'
export default {
  name: 'ParsedDialog',
  data() {
    return {
      tableLoading: false,
      confirmLoading: false,
      dialogVisible: false,
      tableData: [],
      createData: [],
      total: 0,
      successParse: 0,
      failedParse: 0,
      tableTitle: [
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
      ]
    }
  },
  watch: {
    dialogVisible(val) {
      if (!val) {
        this.tableData = []
        this.createData = []
        this.total = 0
        this.successParse = 0
        this.failedParse = 0
      }
    }
  },
  methods: {
    showDialog(path) {
      this.dialogVisible = true
      this.getInfo(path)
    },
    getInfo(path) {
      this.tableLoading = true
      getParsedList(path).then(res => {
        const { data } = res
        this.tableData = [...data]
        this.total = data.length
        this.tableLoading = false
        for (let i = 0; i < this.total; i++) {
          if (data[i].successImport) this.successParse += 1
          else this.failedParse += 1
        }
      })
    },
    handleSelectChange(selection) {
      this.createData = selection
    },
    handleConfirm() {
      if (this.createData.length === 0) {
        this.$message({
          message: '请选择需要添加信息的课程',
          type: 'warning'
        })
        return
      }
      const params = [...this.createData]
      this.confirmLoading = true
      createBatchInfo(params).then(res => {
        this.$message({
          message: '添加成功',
          type: 'success'
        })
        this.$emit('create-success')
        this.confirmLoading = false
        this.dialogVisible = false
      })
    },
    handleSelectable(row, index) {
      if (row.successImport) return true
      return false
    },
    handleRowStyle({ row, rowIndex }) {
      if (row.successImport) {
        return {
          backgroundColor: '#e9ffea'
        }
      } else {
        return {
          backgroundColor: '#fff4f4'
        }
      }
    }
  }
}
</script>

<style lang="scss">
.parsed-dialog {
    .parseTip {
        font-size: 16px;
        font-weight: 700;
        margin-bottom: 10px;
    }
}
</style>

