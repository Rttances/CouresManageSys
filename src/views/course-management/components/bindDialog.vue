<template>
  <div>
    <el-dialog
      class="bind-dialog"
      title="绑定信息"
      :visible.sync="dialogVisible"
      width="27%"
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
        <el-form-item label="教师编号" prop="teacherId">
          <!-- <el-input
            v-model="formData.teacherId"
            :disabled="isEdit"
            placeholder="请输入教师编号"
          /> -->
          <!-- <el-select v-model="formData.teacherId" filterable placeholder="请输入教师编号" filter-method> -->
          <el-select
            v-model="formData.teacherId"
            :loading="teacherLoading"
            filterable
            placeholder="请选择教师编号"
            :filter-method="fetchTeacherOption"
            style="width: 200px"
            @change="handleTeacherName"
          >
            <el-option
              v-for="item in teacherOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="教师名称" prop="teacherName">
          <el-input
            v-model="formData.teacherName"
            :disabled="true"
            placeholder="请输入名称"
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="教学组编号" prop="teachClassGroupId">
          <el-select
            v-model="formData.teachClassGroupId"
            :loading="teachGroupLoading"
            filterable
            placeholder="请选择教学组编号"
            :filter-method="fetchTeachGroupOption"
            style="width: 200px"
          >
            <el-option
              v-for="item in teachGroupOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="课程类型" prop="courseType">
          <el-select v-model="formData.courseType" style="width: 200px" placeholder="请选择课程类型">
            <el-option
              v-for="item in courseType"
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
  createSchedule,
  updateSchedule
} from '@/api/schedule-management'
import { getTeacherList } from '@/api/teacher-management'
import { getTeachClassList } from '@/api/class-management'
export default {
  name: 'BindDialog',
  data() {
    return {
      formLoading: false,
      confirmLoading: false,
      isEdit: false,
      courseType: [
        {
          label: '课程',
          value: 0
        },
        {
          label: '实验',
          value: 1
        }
      ],
      teacherLoading: false,
      teacherOptions: [],
      teachGroupLoading: false,
      teachGroupOptions: [],
      courseOptions: [],
      formData: {
        courseId: '',
        teacherId: '',
        teacherName: '',
        teachClassGroupId: '',
        courseType: ''
      },
      dialogVisible: false,
      rules: {
        teacherId: [
          {
            required: true,
            message: '请输入教师编号',
            trigger: 'change'
          }
        ],
        teacherName: [
          {
            required: true,
            message: '请输入教师名称',
            trigger: 'change'
          }
        ],
        teachClassGroupId: [
          {
            required: true,
            message: '请输入教学组编号',
            trigger: 'change'
          }
        ],

        courseType: [
          {
            required: true,
            message: '请输入课程类型',
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
          teacherId: '',
          teacherName: '',
          teachClassGroupId: '',
          courseType: ''
        }
        this.isEdit = false
      }
      this.fetchTeacherOption()
      this.fetchTeachGroupOption()
    }
  },
  methods: {
    fetchTeacherOption() {
      const params = {
        entity: {
          teacherId: ''
        },
        isAll: true
      }
      this.teacherLoading = true
      getTeacherList(params).then(res => {
        const { data } = res
        const { records } = data
        this.teacherOptions = records.map(item => {
          return {
            label: item.teacherId,
            value: item.teacherId,
            nameValue: item.teacherName
          }
        })
        this.teacherLoading = false
      }).finally(() => {
        this.teacherLoading = false
      })
    },
    fetchTeachGroupOption() {
      const params = {
        teachClassGroupId: ''
      }
      this.teachGroupLoading = true
      getTeachClassList(params).then(res => {
        const { data } = res
        this.teachGroupOptions = data.map(item => {
          return {
            label: item.teachClassGroupId,
            value: item.teachClassGroupId,
            nameValue: item.groupName
          }
        })
        this.teachGroupLoading = false
      }).finally(() => {
        this.teachGroupLoading = false
      })
    },
    handleTeacherName(val) {
      this.teacherOptions.forEach(item => {
        if (val === item.value) {
          this.formData.teacherName = item.nameValue
        }
      })
    },
    showDialog(id, row) {
      this.dialogVisible = true
      if (row) {
        // const { courseId } = row
        // this.getInfo(courseId)
        this.isEdit = true
      }
      this.formData.courseId = id
    },
    handleConfirm() {
      this.$refs.courseForm.validate((valid) => {
        if (valid) {
          const params = Object.assign({}, this.formData)
          const apiMethod = this.isEdit ? updateSchedule : createSchedule
          this.confirmLoading = true
          apiMethod(params).then(res => {
            this.$message({
              message: this.isEdit ? '编辑成功' : '绑定成功',
              type: 'success'
            })
            this.confirmLoading = false
            this.dialogVisible = false
            this.$emit('create-success')
          })
        }
      })
    }
    // getInfo(id) {
    //   this.formLoading = true
    //   getCourseInfo(id).then(res => {
    //     const { data } = res
    //     this.formData = {
    //       ...data
    //     }
    //     this.formLoading = false
    //   })
  }
}
</script>

  <style lang="scss" >

  </style>
