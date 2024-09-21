<template>
  <div>
    <el-dialog
      class="edit-dialog"
      :title="isEdit ? '编辑信息' : '新增信息'"
      :visible.sync="dialogVisible"
      width="30%"
    >
      <el-form
        ref="classForm"
        v-loading="formLoading"
        label-position="right"
        label-width="100px"
        :model="formData"
        :rules="rules"
        @submit.native.prevent
      >
        <el-form-item label="班级名称" prop="className">
          <el-input
            v-model="formData.className"
            placeholder="请输入班级名称"
          />
        </el-form-item>
        <el-form-item label="年级" prop="grade">
          <el-input
            v-model="formData.grade"
            placeholder="请输入年级"
          />
        </el-form-item>
        <el-form-item label="专业名称" prop="majorName">
          <el-input
            v-model="formData.majorName"
            placeholder="请输入专业名称"
          />
        </el-form-item>
        <el-form-item label="方向名称" prop="directionName">
          <el-input
            v-model="formData.directionName"
            placeholder="请输入方向名称"
          />
        </el-form-item>
        <el-form-item label="学生人数" prop="studentNumber">
          <el-input
            v-model="formData.studentNumber"
            placeholder="请输入学生人数"
          />
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
  getClassInfo,
  createClassInfo,
  editClassInfo
} from '@/api/class-management'
export default {
  name: 'ClassDialog',
  data() {
    return {
      formLoading: false,
      confirmLoading: false,
      isEdit: false,
      formData: {
        className: '',
        grade: '',
        majorName: '',
        directionName: '',
        studentNumber: ''
      },
      dialogVisible: false,
      rules: {
        className: [
          {
            required: true,
            message: '请输入班级名称',
            trigger: 'change'
          }
        ],
        grade: [
          {
            required: true,
            message: '请输入年级',
            trigger: 'change'
          }
        ],
        majorName: [
          {
            required: true,
            message: '请输入专业名称',
            trigger: 'change'
          }
        ],
        studentNumber: [
          {
            required: true,
            message: '请输入学生人数',
            trigger: 'change'
          }
        ]
      }
    }
  },
  watch: {
    dialogVisible(val) {
      if (!val) {
        this.formData = {
          className: '',
          grade: '',
          majorName: '',
          directionName: '',
          studentNumber: ''
        }
        this.isEdit = false
      }
    }
  },
  methods: {
    showDialog(row) {
      this.dialogVisible = true
      if (row) {
        const { classId } = row
        this.isEdit = true
        this.getInfo(classId)
      }
    },
    getInfo(id) {
      this.formLoading = true
      getClassInfo(id).then(res => {
        const { data } = res
        this.formData = {
          ...data
        }
        this.formLoading = false
      })
    },
    handleConfirm() {
      this.$refs.classForm.validate((valid) => {
        if (valid) {
          const params = Object.assign({}, this.formData)
          const apiMethod = this.isEdit ? editClassInfo : createClassInfo
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
