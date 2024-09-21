<template>
  <div class="class-table">
    <div class="table-wrapper">
      <div class="tabel-container">

        <table>
          <thead>
            <tr>
              <th>时间</th>
              <th v-for="(weekNum, weekIndex) in classTableData.courses.length" :key="weekIndex"> {{ '周' + digital2Chinese(weekIndex, 'week') }}</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(lesson, lessonIndex) in classTableData.lessons" :key="lessonIndex">
              <td>
                <p>{{ lesson }}</p>
              </td>

              <td v-for="(course, courseIndex) in classTableData.courses" :key="courseIndex">
                {{ classTableData.courses[courseIndex][lessonIndex] || '-' }}
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  props: {
    chartData: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      classTableData: {
        lessons: [
          '第一节',
          '第二节',
          '第三节',
          '第四节',
          '第五节',
          '第六节',
          '第七节'
        ],
        courses: [
          ['', '', '', '', '', '', ''],
          ['', '', '', '', '', '', ''],
          ['', '', '', '', '', '', ''],
          ['', '', '', '', '', '', ''],
          ['', '', '', '', '', '', ''],
          ['', '', '', '', '', '', ''],
          ['', '', '', '', '', '', '']
        ]
      }

    }
  },
  created() {
    this.handleChartData()
  },
  methods: {
    handleChartData() {
      this.chartData.forEach(item => {
        const { courseName, weekDay, daySectionStart, daySectionEnd } = item
        const lessonStart = daySectionStart - 1
        const lessonEnd = daySectionEnd - 1
        for (let i = lessonStart; i <= lessonEnd; i++) {
          this.classTableData.courses[weekDay][i] = courseName
        }
      })
    },
    /**
      * 数字转中文
      * @param {Number} num 需要转换的数字
      * @param {String} identifier 标识符
      * @returns {String} 转换后的中文
      */
    digital2Chinese(num, identifier) {
      const character = ['零', '一', '二', '三', '四', '五', '六', '七', '八', '九', '十', '十一', '十二']
      return identifier === 'week' && (num === 0 || num === 7) ? '日' : character[num]
    }
  }
}
</script>

<style lang="scss" scoped>
.class-table {
  .table-wrapper {
      width: 100%;
      height: 100%;
      overflow: auto;
  }
  .tabel-container {
      margin: 7px;

      table {
          table-layout: fixed;
          width: 100%;

          thead {
              background-color: #67a1ff;
              th {
                  color: #fff;
                  line-height: 17px;
                  font-weight: normal;
              }
          }
          tbody {
              background-color: #eaf2ff;
              td {
                  color: #677998;
                  line-height: 12px;
              }
          }
          th,
          td {
              width: 60px;
              padding: 12px 2px;
              font-size: 12px;
              text-align: center;
          }

          tr td:first-child {
              color: #333;
              .period {
                  font-size: 8px;
              }
          }
      }
  }
}
</style>
