export function getOptions(lesson) {
  const options = [
    {
      value: 1,
      label: '星期一'
    },
    {
      value: 2,
      label: '星期二'
    },
    {
      value: 3,
      label: '星期三'
    },
    {
      value: 4,
      label: '星期四'
    },
    {
      value: 5,
      label: '星期五'
    },
    {
      value: 6,
      label: '星期六'
    },
    {
      value: 7,
      label: '星期日'
    }
  ]
  const oneLessonChildren = [
    {
      value: [1, 1],
      label: '第一节'
    },
    {
      value: [3, 3],
      label: '第三节'
    },
    {
      value: [5, 5],
      label: '第五节'
    }
  ]

  const twoLessonsChildren = [
    {
      value: [1, 2],
      label: '第一、二节'
    },
    {
      value: [3, 4],
      label: '第三、四节'
    },
    {
      value: [5, 6],
      label: '第五、六节'
    }
  ]

  const threeLessonsChildren = [
    {
      value: [5, 7],
      label: '第五、六、七节'
    }
  ]

  const fourLessonsChildren = [
    {
      value: [1, 4],
      label: '第一、二、三、四节'
    }
  ]
  if (lesson === 1) {
    return options.map(item => {
      item.children = [...oneLessonChildren]
      return item
    })
  }
  if (lesson === 2) {
    return options.map(item => {
      item.children = [...twoLessonsChildren]
      return item
    })
  }
  if (lesson === 3) {
    return options.map(item => {
      item.children = [...threeLessonsChildren]
      return item
    })
  }
  if (lesson === 4) {
    return options.map(item => {
      item.children = [...fourLessonsChildren]
      return item
    })
  }
}

