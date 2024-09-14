import request from '@/utils/request'

// export function addVolunteerChoice(data) { // 添加选课志愿
//   return request({
//     url: '/school-timetable/course-volunteer/create',
//     method: 'post',
//     data
//   })
// }

export function addVolunteerChoice(data) { // 添加选课志愿
  return request({
    url: '/school-timetable/course-volunteer/create',
    method: 'post',
    data
  })
}

export function changeVolunteerChoice(data) {
  return request({
    url: '/school-timetable/course-volunteer/update',
    method: 'put',
    data
  })
}

export function getScheduleList(data) { // 条件查询课程表
  return request({
    url: '/school-timetable/query',
    method: 'post',
    data
  })
}

export function autoArrangeSchedule() { // 自动分配课程
  return request({
    url: '/school-timetable/auto-schedule',
    method: 'get'
  })
}

export function updateSchedule(data) { // 更新课程表
  return request({
    url: '/school-timetable/update',
    method: 'put',
    data
  })
}

export function createSchedule(data) { // 添加课程表
  return request({
    url: '/school-timetable/create',
    method: 'post',
    data
  })
}

