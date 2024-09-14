a
<template>
  <div>
    <el-dialog
      class="view-dialog"
      title="查看志愿"
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
          <el-input
            v-model="formData.firstChoice"
            style="width: 180px"
            class="cascader"
            :disabled="true"
          />
        </el-form-item>
        <el-form-item label="第二志愿" prop="secondChoice">
          <el-input
            v-model="formData.secondChoice"
            class="cascader"
            :disabled="true"
          />
        </el-form-item>
        <el-form-item label="第三志愿" prop="thirdChoice">
          <el-input
            v-model="formData.thirdChoice"
            class="cascader"
            :disabled="true"
          />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
const dayMap = new Map([
  [1, '星期一'],
  [2, '星期二'],
  [3, '星期三'],
  [4, '星期四'],
  [5, '星期五'],
  [6, '星期六'],
  [7, '星期日']
])

const courseMap = new Map([
  [1, '第一节'],
  [2, '第二节'],
  [3, '第三节'],
  [4, '第四节'],
  [5, '第五节'],
  [6, '第六节'],
  [7, '第七节']
])

export default {
  name: 'ViewDialog',
  data() {
    return {
      formLoading: false,
      confirmLoading: false,
      formData: {
        firstChoice: [],
        secondChoice: [],
        thirdChoice: []
      },
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
      }
    }
  },
  methods: {

    async showDialog(row) {
      this.dialogVisible = true
      const { courseVolunteer } = row
      const { volunteerTime } = courseVolunteer
      volunteerTime.forEach(item => {
        if (item.order === 0) {
          this.formData.firstChoice = `${dayMap.get(item.weekDay)}、 ${courseMap.get(item.daySectionStart)} 至 ${courseMap.get(item.daySectionEnd)}`
        }
        if (item.order === 1) {
          this.formData.secondChoice = `${dayMap.get(item.weekDay)}、 ${courseMap.get(item.daySectionStart)} 至 ${courseMap.get(item.daySectionEnd)}`
        }
        if (item.order === 2) {
          this.formData.thirdChoice = `${dayMap.get(item.weekDay)}、 ${courseMap.get(item.daySectionStart)} 至 ${courseMap.get(item.daySectionEnd)}`
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
