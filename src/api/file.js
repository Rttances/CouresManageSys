import request from '@/utils/request'

export function fileUpload(data) {
  return request({
    url: '/file/upload',
    method: 'post',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data
  })
}
