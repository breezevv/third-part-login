import request from '@/utils/request'

const backend_url = 'http://server.zhengjunren.com:8080'

export function login(data) {
  return request({
    url: '/vue-element-admin/user/login',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: backend_url + '/user/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/vue-element-admin/user/logout',
    method: 'post'
  })
}

export function getAuthorizationUrl(source) {
  return request({
    url: backend_url + '/auth/url/' + source,
    method: 'get'
  })
}
