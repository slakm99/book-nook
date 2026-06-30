import request from '../utils/request'

export function getBookList(params) {
  return request({
    url: '/book/list',
    method: 'get',
    params
  })
}

export function getCategoryList() {
  return request({
    url: '/category/list',
    method: 'get'
  })
}
