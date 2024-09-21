import request from '@/utils/request'

export function getClassList(data) { // 分页条件查询行政班级信息
  return request({
    url: '/administrative-class/page',
    method: 'post',
    data
  })
}

// export function getTeachClassList(data) { // 条件搜索教学班组信息
//   return request({
//     url: '/teach-class-group/search',
//     method: 'post',
//     data
//   })
// }

export function getTeachClassList(data) { // 条件搜索教学班组信息
  return request({
    url: '/administrative-class/teach-class-group/search',
    method: 'post',
    data
  })
}

export function mergeClass(data) { // 合并行政班级
  return request({
    url: '/administrative-class/merge',
    method: 'post',
    data
  })
}

export function createClassInfo(data) { // 新增单条行政班级数据
  return request({
    url: '/administrative-class/create',
    method: 'post',
    data
  })
}

export function editClassInfo(data) { // 更新行政班级信息
  return request({
    url: '/administrative-class/update',
    method: 'put',
    data
  })
}

export function createBatchInfo(data) { // 批量添加行政班级数据
  return request({
    url: '/administrative-class/batch/create',
    method: 'post',
    data
  })
}

export function getTeachClassDetail(data) { // 根据教学班组编号查找信息
  return request({
    url: `/administrative-class/teach-class-group/detail?teachClassGroupId=${data}`,
    method: 'get',
    data
  })
}

// export function getTeachClassDetail(data) { // 根据教学班组编号查找信息
//   return request({
//     url: '/teach-class-group/detail',
//     method: 'get',
//     data
//   })
// }

export function getParsedList(data) { // 解析行政班级 Excel 数据
  return request({
    url: `/administrative-class/import?filename=${data}`,
    method: 'get',
    data
  })
}

export function getDemoPath(data) { // 导出行政班级 Excel 模板
  return request({
    url: '/administrative-class/excel/demo',
    method: 'get',
    data
  })
}

export function getClassInfo(data) { // 查询行政班级信息
  return request({
    url: `/administrative-class/detail/${data}`,
    method: 'get',
    data
  })
}
