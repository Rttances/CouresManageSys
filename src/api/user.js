import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}
export function getRoleInfo(data) {
  return request({
    url: '/user/all-role-permission',
    method: 'get',
    data
  })
}

export function getInfo(data) {
  return request({
    url: `/user/detail/${data}`,
    method: 'get',
    data
    // params: { token }
  })
}

export function logout() {
  return request({
    url: '/vue-admin-template/user/logout',
    method: 'post'
  })
}
