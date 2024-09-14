a
<template>
  <div>
    <el-dialog
      class="arrange-dialog"
      :title="isEdit ? '修改志愿' : '自主排课'"
      :visible.sync="dialogVisible"
      width="30%"
    >
      <el-form
        ref="choiceForm"
        label-position="right"
        label-width="140px"
        :model="formData"
        :rules="rules"
        @submit.native.prevent
      >
        <el-form-item label="第一志愿" prop="firstChoice">
          <el-cascader
            v-model="formData.firstChoice"
            v-loading="optionLoading"
            class="cascader"
            :options="options"
          />
        </el-form-item>
        <el-form-item label="第二志愿" prop="secondChoice">
          <el-cascader
            v-model="formData.secondChoice"
            v-loading="optionLoading"
            class="cascader"
            :options="options"
          />
        </el-form-item>
        <el-form-item label="第三志愿" prop="thirdChoice">
          <el-cascader
            v-model="formData.thirdChoice"
            v-loading="optionLoading"
            class="cascader"
            :options="options"
          />
        </el-form-item>
      </el-form>
      <span style="margin-left: 120px;font-size: 12px;">若不填写志愿则自动安排合适时间段</span>
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
import { addVolunteerChoice, changeVolunteerChoice } from '@/api/schedule-management'
import { getCourseInfo } from '@/api/course-management.js'
import { getOptions } from './options.js'
export default {
  name: 'ClassDialog',
  data() {
    return {
      isEdit: false,
      formLoading: false,
      confirmLoading: false,
      optionLoading: false,
      schoolTimetableId: '',
      courseVolunteerId: '',
      formData: {
        firstChoice: [],
        secondChoice: [],
        thirdChoice: []
      },
      courseId: '',
      options: [],
      dialogVisible: false,
      rules: {
        firstChoice: [
          {
            required: true,
            message: '请选择第一志愿',
            trigger: 'change'
          }
        ],
        secondChoice: [
          {
            required: true,
            message: '请选择第二志愿',
            trigger: 'change'
          }
        ],
        thirdChoice: [
          {
            required: true,
            message: '请选择第三志愿',
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
          firstChoice: [],
          secondChoice: [],
          thirdChoice: []
        }
        this.isEdit = false
        this.schoolTimetableId = ''
        this.courseVolunteerId = ''
        this.courseId = ''
      }
    }
  },
  methods: {
    showDialog(row, edit) {
      this.courseId = courseId
      this.dialogVisible = true
      const { courseId, courseType, schoolTimetableId, courseVolunteer } = row
      this.schoolTimetableId = schoolTimetableId
      this.handleOptions(courseId, courseType)
      if (edit) {
        this.isEdit = edit
        const { courseVolunteerId } = courseVolunteer
        this.courseVolunteerId = courseVolunteerId
      }
    },
    handleOptions(id, type) {
      this.optionLoading = true
      getCourseInfo(id).then(res => {
        const { data } = res
        const { weekTheoreticalClassHour, weekExperimentalClassHour } = data
        this.options = getOptions(type === 0 ? weekTheoreticalClassHour : weekExperimentalClassHour)
        this.optionLoading = false
      })
    },
    checkDifferent(data) {
      for (let i = 0; i <= 2; i++) {
        for (let j = i + 1; j <= 2; j++) {
          if (data[i].weekDay === data[j].weekDay &&
          data[i].daySectionStart === data[j].daySectionStart &&
          data[i].daySectionEnd === data[j].daySectionEnd
          ) {
            return false
          }
        }
      }
      return true
    },
    handleConfirm() {
      this.$refs.choiceForm.validate((valid) => {
        if (valid) {
          const volunteerTime = [
            {
              order: 0,
              weekDay: this.formData.firstChoice[0],
              daySectionStart: this.formData.firstChoice[1][0],
              daySectionEnd: this.formData.firstChoice[1][1]
            },
            {
              order: 1,
              weekDay: this.formData.secondChoice[0],
              daySectionStart: this.formData.secondChoice[1][0],
              daySectionEnd: this.formData.secondChoice[1][1]
            },
            {
              order: 2,
              weekDay: this.formData.thirdChoice[0],
              daySectionStart: this.formData.thirdChoice[1][0],
              daySectionEnd: this.formData.thirdChoice[1][1]
            }
          ]
          if (!this.checkDifferent(volunteerTime)) {
            this.$message({
              message: '请勿选择重复时间段志愿!',
              type: 'warning'
            })
            return
          }
          const params = {
            volunteerTime,
            schoolTimetableId: this.schoolTimetableId,
            courseVolunteerId: this.courseVolunteerId
          }
          this.confirmLoading = true
          const apiMethod = this.isEdit ? changeVolunteerChoice : addVolunteerChoice
          apiMethod(params).then(res => {
            this.$message({
              message: this.isEdit ? '志愿修改成功' : '排课成功',
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

<style lang="scss" scoped>
.cascader {
  width: 250px !important;
}
</style>
