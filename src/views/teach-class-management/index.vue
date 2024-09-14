<template>
  <div class="app-container">
    <el-card class="search-box">
      <el-form
        :inline="true"
        :model="searchParams"
        @submit.native.prevent
      >
        <el-form-item label="教学班级组编号">
          <el-input v-model="searchParams.teachClassGroupId" placeholder="教学班级组编号" />
        </el-form-item>
        <el-form-item label="教学班级组名称">
          <el-input v-model="searchParams.groupName" placeholder="教学班级组名称" />
        </el-form-item>
        <el-form-item label="行政班级">
          <el-select v-model="searchParams.classIdSet" multiple placeholder="行政班级">
            <el-option
              v-for="item in classOptions"
              :key="item.classId"
              :value="item.classId"
              :label="item.majorName + item.className "
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            icon="el-icon-search"
            @click="fetchData"
          >查询</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="table-box">
      <el-table
        ref="teachClassTable"
        v-loading="tableLoading"
        fit
        :data="tableData"
        element-loading-text="Loading"
      >
        <el-table-column
          v-for="item in tableTitle"
          :key="item.id"
          :prop="item.id"
          :label="item.label"
          align="center"
        >
          <template slot-scope="scope">
            <span v-if="item.id !== 'administrativeClassVOS'">{{ scope.row[item.id] }}</span>
            <span v-else>
              <el-tag
                v-for="teachClass in scope.row[item.id]"
                :key="teachClass.classId"
                style="margin-left: 5px;margin-bottom: 5px;"
                type="success"
                disable-transitions
              >{{ teachClass.majorName + ' ' + teachClass.className }}
              </el-tag>
            </span>
          </template>
        </el-table-column>
        <el-table-column class-name="status-col" label="操作" width="150" align="center">
          <template slot-scope="scope">
            <el-button type="text" @click="detail(scope.row)">详细</el-button>
            <el-button type="text" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <TeachClassDialog ref="teachClassDialog" />
  </div>
</template>

<script>
import TeachClassDialog from './components/teachClassDialog.vue'
import { getClassList, getTeachClassList } from '@/api/class-management'
export default {
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'gray',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  components: {
    TeachClassDialog
  },
  data() {
    return {
      tableLoading: false,
      editVisible: false,
      deleteVisible: false,
      classOptions: [],
      tableTitle: [
        {
          id: 'teachClassGroupId',
          label: '教学班级组编号'
        },
        {
          id: 'groupName',
          label: '教学班级组名称'
        },
        {
          id: 'studentTotal',
          label: '总学生人数'
        },
        {
          id: 'administrativeClassVOS',
          label: '行政班级'
        }
      ],
      tableData: [],
      selectionTable: [],
      searchParams: {
        teachClassGroupId: '',
        classIdSet: [],
        groupName: ''
      }
    }
  },
  created() {
    this.fetchData()
    this.fetchClassOption()
  },
  methods: {
    fetchClassOption() {
      const params = {
        isAll: true
      }
      getClassList(params).then(response => {
        const { data } = response
        const { records } = data
        this.classOptions = records
      })
    },
    fetchData() {
      this.tableLoading = true
      const {
        teachClassGroupId,
        classIdSet,
        groupName
      } = this.searchParams
      const params = {
        teachClassGroupId,
        classIdSet,
        groupName
      }
      getTeachClassList(params).then(response => {
        const { data } = response
        this.tableData = [...data]
        this.tableLoading = false
      }).finally(() => {
        this.tableLoading = false
      })
    },
    detail(row) {
      const { teachClassGroupId } = row
      this.$refs.teachClassDialog.showDialog(teachClassGroupId)
    },
    handleDelete() {

    }
  }
}
</script>

  <style lang="scss">
  .app-container {
    .search-box {
      padding: 0 18px;
      margin-bottom: 10px;
    }
    .table-box {
      .clearfix {
        padding: 0 18px;
      }
    }
  }
  </style>
