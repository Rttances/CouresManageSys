import request from '@/utils/request'

export function getCourseList(data) { // 分页条件查询课程信息
  return request({
    url: '/course/page',
    method: 'post',
    data
  })
}

export function bindCourseTeacher(data) { // 课程绑定教师
  return request({
    url: '/course/map-teacher',
    method: 'post',
    data
  })
}

export function updateCourseInfo(data) { // 更新课程信息
  return request({
    url: '/course/update',
    method: 'put',
    data
  })
}

export function createBatchInfo(data) { // 批量添加 Excel 数据
  return request({
    url: '/course/batch/create/excel',
    method: 'post',
    data
  })
}

export function createCourseInfo(data) { // 添加单个 Excel 数据
  return request({
    url: '/course/create',
    method: 'post',
    data
  })
}

export function getDemoPath(data) { // 导出课程 Excel 模板
  return request({
    url: '/course/excel/demo',
    method: 'get',
    data
  })
}

export function getCourseInfo(data) { // 根据课程编号查询课程信息
  return request({
    url: `/course/detail/${data}`,
    method: 'get',
    data
  })
}

export function getParsedList(data) { // 解析课程 Excel 数据
  return request({
    url: `/course/import?filename=${data}`,
    method: 'get',
    data
  })
}

