<template>
  <div>
    <el-dialog
      class="edit-dialog"
      :title="isEdit ? '编辑信息' : '新增信息'"
      :visible.sync="dialogVisible"
      width="35%"
    >
      <el-form
        ref="teacherForm"
        v-loading="formLoading"
        label-position="right"
        label-width="80px"
        :model="formData"
        :rules="rules"
        @submit.native.prevent
      >
        <el-form-item label="教师编号" prop="teacherName">
          <el-input
            v-model="formData.teacherId"
            placeholder="请输入教师编号"
            :disabled="isEdit"
          /></el-form-item>
        <el-form-item label="教师姓名" prop="teacherName">
          <el-input
            v-model="formData.teacherName"
            placeholder="请输入教师姓名"
          /></el-form-item>
        <el-form-item label="职称" prop="teacherLevel">
          <el-select v-model="formData.teacherLevel" placeholder="职称">
            <el-option
              v-for="item in levelOptions"
              :key="item.value"
              :value="item.value"
              :label="item.label"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button
          v-loading="confirmLoading"
          type="primary"
          @click="handleConfirm"
        >确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getTeacherInfo,
  createTeacherInfo,
  editTeacherInfo
} from '@/api/teacher-management'
export default {
  name: 'TeacherDialog',
  data() {
    return {
      formLoading: false,
      confirmLoading: false,
      isEdit: false,
      formData: {
        teacherName: '',
        teacherLevel: ''
      },
      dialogVisible: false,
      rules: {
        teacherName: [
          {
            required: true,
            message: '请输入教师姓名',
            trigger: 'change'
          }
        ],
        teacherLevel: [
          {
            required: true,
            message: '请选择职称',
            trigger: 'change'
          }
        ]
      },
      levelOptions: [
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
      ]
    }
  },
  watch: {
    dialogVisible(val) {
      if (!val) {
        this.formData = {
          teacherName: '',
          teacherLevel: ''
        }
        this.isEdit = false
      }
    }
  },
  methods: {
    showDialog(row) {
      this.dialogVisible = true
      if (row) {
        const { teacherId } = row
        this.getInfo(teacherId)
        this.isEdit = true
      }
    },
    getInfo(id) {
      this.formLoading = true
      getTeacherInfo(id).then(res => {
        const { data } = res
        this.formData = {
          ...data
        }
        this.formLoading = false
      })
    },
    handleConfirm() {
      this.$refs.teacherForm.validate((valid) => {
        if (valid) {
          const params = Object.assign({}, this.formData)
          const apiMethod = this.isEdit ? editTeacherInfo : createTeacherInfo
          this.confirmLoading = true
          apiMethod(params).then(res => {
            this.$message({
              message: this.isEdit ? '编辑成功' : '创建成功',
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

<style lang="scss" >

</style>
