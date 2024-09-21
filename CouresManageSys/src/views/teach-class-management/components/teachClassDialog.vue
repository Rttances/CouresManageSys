<template>
  <div>
    <el-dialog
      class="teach-class-dialog"
      title="教学班详细"
      :visible.sync="dialogVisible"
      width="50%"
    >
      <el-table
        ref="teachClassTable"
        v-loading="tableLoading"
        style="margin-bottom: 20px;"
        :data="tableData"
        element-loading-text="Loading"
        fit
      >
        <el-table-column
          v-for="item in tableTitle"
          :key="item.id"
          :prop="item.id"
          :label="item.label"
          :disabled="item"
          align="center"
        />
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getTeachClassDetail
} from '@/api/class-management'
export default {
  name: 'TeachClassTable',
  data() {
    return {
      tableLoading: false,
      confirmLoading: false,
      dialogVisible: false,
      tableData: [],
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
      ]
    }
  },
  watch: {
    dialogVisible(val) {
      if (!val) {
        this.tableData = []
      }
    }
  },
  methods: {
    showDialog(id) {
      this.dialogVisible = true
      if (id) {
        this.fetchTableData(id)
      }
    },
    fetchTableData(id) {
      // const params = {
      //   teachClassGroupId: id
      // }
      this.tableLoading = true
      getTeachClassDetail(id).then(response => {
        const { data } = response
        const { administrativeClassVOS } = data
        this.tableData = [...administrativeClassVOS]
        this.tableLoading = false
      })
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

