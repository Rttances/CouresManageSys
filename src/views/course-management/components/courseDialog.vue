<template>
  <div>
    <el-dialog
      class="edit-dialog"
      :title="isEdit ? '编辑信息' : '新增信息'"
      :visible.sync="dialogVisible"
      width="30%"
    >
      <el-form
        ref="courseForm"
        v-loading="formLoading"
        label-position="right"
        label-width="100px"
        :model="formData"
        :rules="rules"
        @submit.native.prevent
      >
        <el-form-item label="课程编号" prop="courseId">
          <el-input
            v-model="formData.courseId"
            :disabled="isEdit"
            placeholder="请输入课程编号"
          />
        </el-form-item>
        <el-form-item label="课程名称" prop="courseName">
          <el-input
            v-model="formData.courseName"
            placeholder="请输入课程名称"
          />
        </el-form-item>
        <el-form-item label="学分" prop="score">
          <el-input
            v-model="formData.score"
            placeholder="请输入学分"
          />
        </el-form-item>
        <el-form-item label="总课时" prop="totalClassHour">
          <el-input
            v-model="formData.totalClassHour"
            placeholder="请输入总课时"
          />
        </el-form-item>
        <el-form-item label="理论课时" prop="theoreticalClassHour">
          <el-input
            v-model="formData.theoreticalClassHour"
            placeholder="请输入理论课时"
          />
        </el-form-item>
        <el-form-item label="实验课时" prop="experimentalClassHour">
          <el-input
            v-model="formData.experimentalClassHour"
            placeholder="请输入实验课时"
          />
        </el-form-item>
        <el-form-item label="周理论课时" prop="weekTheoreticalClassHour">
          <el-input
            v-model="formData.weekTheoreticalClassHour"
            placeholder="请输入周理论课时"
          />
        </el-form-item>
        <el-form-item label="周实验课时" prop="weekExperimentalClassHour">
          <el-input
            v-model="formData.weekExperimentalClassHour"
            placeholder="请输入周实验课时"
          /></el-form-item>
        <el-form-item label="起始周" prop="startWeek">
          <el-input
            v-model="formData.startWeek"
            placeholder="请输入起始周"
          />
        </el-form-item>
        <el-form-item label="结束周" prop="endWeek">
          <el-input
            v-model="formData.endWeek"
            placeholder="请输入结束周"
          />
        </el-form-item>
        <el-form-item label="归属系" prop="belongSystem">
          <el-input
            v-model="formData.belongSystem"
            placeholder="请输入归属系"
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
  getCourseInfo,
  updateCourseInfo,
  createCourseInfo
} from '@/api/course-management'
export default {
  name: 'CourseDialog',
  data() {
    return {
      formLoading: false,
      confirmLoading: false,
      isEdit: false,
      formData: {
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
      },
      dialogVisible: false,
      rules: {
        courseId: [
          {
            required: true,
            message: '请输入课程编号',
            trigger: 'change'
          }
        ],
        score: [
          {
            required: true,
            message: '请输入教师学分',
            trigger: 'change'
          }
        ],
        totalClassHour: [
          {
            required: true,
            message: '请输入总课时',
            trigger: 'change'
          }
        ],
        theoreticalClassHour: [
          {
            required: true,
            message: '请输入理论课时',
            trigger: 'change'
          }
        ],
        experimentalClassHour: [
          {
            required: true,
            message: '请输入实验课时',
            trigger: 'change'
          }
        ],
        weekTheoreticalClassHour: [
          {
            required: true,
            message: '请输入周理论课时',
            trigger: 'change'
          }
        ],
        weekExperimentalClassHour: [
          {
            required: true,
            message: '请输入周实验课时',
            trigger: 'change'
          }
        ],
        startWeek: [
          {
            required: true,
            message: '请输入起始周',
            trigger: 'change'
          }
        ],
        endWeek: [
          {
            required: true,
            message: '请输入结束周',
            trigger: 'change'
          }
        ],
        belongSystem: [
          {
            required: true,
            message: '请输入归属系',
            trigger: 'change'
          }
        ],
        courseName: [
          {
            required: true,
            message: '请输入课程名称',
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
        this.isEdit = false
      }
    }
  },
  methods: {
    showDialog(row) {
      this.dialogVisible = true
      if (row) {
        this.isEdit = true
        const { courseId } = row
        this.getInfo(courseId)
      }
    },
    getInfo(id) {
      this.formLoading = true
      getCourseInfo(id).then(res => {
        const { data } = res
        this.formData = {
          ...data
        }
        this.formLoading = false
      })
    },
    handleConfirm() {
      this.$refs.courseForm.validate((valid) => {
        if (valid) {
          const params = Object.assign({}, this.formData)
          const apiMethod = this.isEdit ? updateCourseInfo : createCourseInfo
          this.confirmLoading = true
          apiMethod(params).then(res => {
            this.$message({
              message: this.isEdit ? '编辑成功' : '添加成功',
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
