<template>
  <div>
    <el-dialog
      class="update-dialog"
      title="更新排课"
      :visible.sync="dialogVisible"
      width="50%"
    >
      <el-form
        ref="updateForm"
        v-loading="formLoading"
        label-position="right"
        label-width="100px"
        :model="formData"
        :rules="rules"
        @submit.native.prevent
      >
        <el-form-item label="周几" prop="weekDay">
          <el-select
            v-model="formData.weekDay"
            placeholder="请输入周几"
          >
            <el-option
              v-for="item in weekDayOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="起始节" prop="daySectionStart">
          <el-select
            v-model="formData.daySectionStart"
            placeholder="请输入起始节"
          >
            <el-option
              v-for="item in sectionOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="结束节" prop="daySectionEnd">
          <el-select
            v-model="formData.daySectionEnd"
            placeholder="请输入结束节"
          >
            <el-option
              v-for="item in sectionOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
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
  updateSchedule
} from '@/api/schedule-management'
export default {
  name: 'UpdateDialog',
  data() {
    return {
      formLoading: false,
      confirmLoading: false,
      formData: {
        weekDay: '',
        daySectionStart: '',
        daySectionEnd: ''
      },
      rowData: {},
      dialogVisible: false,
      weekDayOptions: [
        {
          label: '星期一',
          value: 1
        },
        {
          label: '星期二',
          value: 2
        },
        {
          label: '星期三',
          value: 3
        },
        {
          label: '星期四',
          value: 4
        },
        {
          label: '星期五',
          value: 5
        },
        {
          label: '星期六',
          value: 6
        },
        {
          label: '星期日',
          value: 7
        }
      ],
      sectionOptions: [
        {
          label: '第一节',
          value: 1
        },
        {
          label: '第二节',
          value: 2
        },
        {
          label: '第三节',
          value: 3
        },
        {
          label: '第四节',
          value: 4
        },
        {
          label: '第五节',
          value: 5
        },
        {
          label: '第六节',
          value: 6
        },
        {
          label: '第七节',
          value: 7
        }
      ],
      rules: {
        weekDay: [
          {
            required: true,
            message: '请输入周几',
            trigger: 'change'
          }
        ],
        daySectionStart: [
          {
            required: true,
            message: '请输入起始节',
            trigger: 'change'
          }
        ],
        daySectionEnd: [
          {
            required: true,
            message: '请输入结束节',
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
          weekDay: '',
          daySectionStart: '',
          daySectionEnd: ''
        }
      }
    }
  },
  methods: {
    showDialog(row) {
      this.dialogVisible = true
      this.rowData = row
    },
    handleConfirm() {
      this.$refs.updateForm.validate((valid) => {
        if (valid) {
          const {
            schoolTimetableId,
            teachClassGroupId,
            courseName,
            teacherId,
            teacherName,
            courseId
          } = this.rowData
          const params = {
            schoolTimetableId,
            teachClassGroupId,
            courseName,
            teacherId,
            teacherName,
            courseId,
            weekDay: this.formData.weekDay,
            daySectionStart: this.formData.daySectionStart,
            daySectionEnd: this.formData.daySectionEnd
          }
          this.confirmLoading = true
          updateSchedule(params).then(res => {
            this.$message({
              message: '更新成功',
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
