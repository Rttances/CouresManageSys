<template>
  <div>
    <el-dialog
      class="merge-dialog"
      title="合并教学班级"
      :visible.sync="dialogVisible"
      width="80%"
    >
      <el-table
        ref="parsedtable"
        v-loading="tableLoading"
        height="auto"
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
      <el-form
        ref="classForm"
        label-position="right"
        label-width="140px"
        :model="formData"
        :rules="rules"
        @submit.native.prevent
      >
        <el-form-item label="教学班级组名称" prop="groupName">
          <el-input
            v-model="formData.groupName"
            placeholder="请输入教学班级组名称"
          />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button
          v-loading="confirmLoading"
          type="primary"
          @click="handleConfirm"
        >合并</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  mergeClass
} from '@/api/class-management'
export default {
  name: 'MergeDialog',
  data() {
    return {
      tableLoading: false,
      confirmLoading: false,
      dialogVisible: false,
      tableData: [],
      total: 0,
      formData: {
        groupName: ''
      },
      rules: {
        groupName: [
          {
            required: true,
            message: '请输入教学班级组名称',
            trigger: 'change'
          }
        ]
      },
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
        this.formData = {}
      }
    }
  },
  methods: {
    showDialog(row) {
      this.dialogVisible = true
      this.tableData = row
    },
    handleConfirm() {
      this.$refs.classForm.validate((valid) => {
        if (valid) {
          const classIdSet = this.tableData.map(item => {
            return item.classId
          })
          const params = {
            classIdSet,
            ...this.formData
          }
          this.confirmLoading = true
          mergeClass(params).then(res => {
            this.$message({
              message: '合并成功',
              type: 'success'
            })
            this.confirmLoading = false
            this.dialogVisible = false
            this.$emit('create-success')
          })
        }
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

