<template>
  <div>
    <el-dialog
      class="upload-dialog"
      title="导入课程信息"
      :visible.sync="dialogVisible"
      width="35%"
    >
      <el-form
        ref="uploadForm"
        v-loading="formLoading"
        label-width="80px"
        @submit.native.prevent
      >
        <el-form-item label="信息文件" prop="teacherId">
          <el-upload
            class="upload-box"
            drag
            action
            :http-request="handleRequest"
            :file-list="fileList"
            accept=".xlsx, .xls"
          >
            <i class="el-icon-upload" />
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div slot="tip" class="el-upload__tip">只能上传xls/xlsx文件, 请严格按照模版填写</div>
          </el-upload>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button
          type="primary"
          :loading="confirmLoading"
          @click="handleConfirm"
        >确 定</el-button>
      </span>
    </el-dialog>
    <ParsedDialog ref="parsedDialog" @create-success="handleSuccess" />
  </div>
</template>

<script>
import { fileUpload } from '@/api/file'
import ParsedDialog from './parsedDialog.vue'
export default {
  name: 'UploadDialog',
  components: {
    ParsedDialog
  },
  data() {
    return {
      formLoading: false,
      confirmLoading: false,
      fileList: [],
      excelPath: '',
      dialogVisible: false
    }
  },
  watch: {
    dialogVisible(val) {
      if (!val) {
        this.fileList = []
        this.excelPath = ''
      }
    }
  },
  methods: {
    showDialog() {
      this.dialogVisible = true
    },
    handleConfirm() {
      if (!this.excelPath) {
        this.$message({
          message: '请上传需要解析的文件',
          type: 'warning'
        })
        return
      }
      this.$refs.parsedDialog.showDialog(this.excelPath)
      this.dialogVisible = false
    },
    handleRequest(event) {
      const { file } = event
      const formData = new FormData()
      formData.append('file', file)
      this.confirmLoading = true
      fileUpload(formData).then(res => {
        const { data } = res
        this.excelPath = data
        this.fileList = [file]
        this.confirmLoading = false
      })
    },
    handleSuccess() {
      this.$emit('create-success')
    }
  }
}
</script>

  <style>

  </style>

