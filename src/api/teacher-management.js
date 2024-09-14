import request from '@/utils/request'

export function getTeacherList(data) { // 分页查询教师信息
  return request({
    url: '/teacher/page',
    method: 'post',
    data
  })
}

export function getTeacherInfo(data) { // 根据教师编号查询教师信息
  return request({
    url: `/teacher/detail/${data}`,
    method: 'get',
    data
  })
}

export function createTeacherInfo(data) { // 单个添加教师信息
  return request({
    url: '/teacher/create',
    method: 'post',
    data
  })
}

export function editTeacherInfo(data) { // （缺失）更新教师信息
  return request({
    url: '/teacher/update',
    method: 'put',
    data
  })
}

export function createBatchInfo(data) { // 批量添加教师信息
  return request({
    url: '/teacher/batch/create',
    method: 'post',
    data
  })
}

export function getParsedList(data) { // Excel 解析教师信息
  return request({
    url: `/teacher/import?filename=${data}`,
    method: 'get',
    data
  })
}

export function getDemoPath(data) { // 导出教师信息 Excel 模板
  return request({
    url: '/teacher/export/demo',
    method: 'get',
    data
  })
}

