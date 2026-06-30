import request from '../utils/request'

export const api = {
  categories: () => request({ url: '/category/list' }),
  saveCategory: (data) => request({ url: data.id ? `/category/${data.id}` : '/category', method: data.id ? 'put' : 'post', data }),
  deleteCategory: (id) => request({ url: `/category/${id}`, method: 'delete' }),

  saveBook: (data) => request({ url: data.id ? `/book/${data.id}` : '/book', method: data.id ? 'put' : 'post', data }),
  deleteBook: (id) => request({ url: `/book/${id}`, method: 'delete' }),

  readers: (params) => request({ url: '/reader/list', params }),
  saveReader: (data) => request({ url: data.id ? `/reader/${data.id}` : '/reader', method: data.id ? 'put' : 'post', data }),
  deleteReader: (id) => request({ url: `/reader/${id}`, method: 'delete' }),

  borrows: (params) => request({ url: '/borrow/list', params }),
  borrow: (data) => request({ url: '/borrow', method: 'post', data }),
  returnBook: (id) => request({ url: `/borrow/${id}/return`, method: 'put' }),

  reservations: (params) => request({ url: '/reservation/list', params }),
  reserve: (data) => request({ url: '/reservation', method: 'post', data }),

  inventoryLogs: (params) => request({ url: '/inventory-log/list', params }),
  goals: (params) => request({ url: '/reading-goal/list', params }),
  saveGoal: (data) => request({ url: '/reading-goal', method: 'post', data }),

  overview: () => request({ url: '/dashboard/overview' }),
  trend: () => request({ url: '/dashboard/trend' }),
  categoryDistribution: () => request({ url: '/dashboard/category-distribution' }),
  recommendation: () => request({ url: '/recommendation/me' }),
  blindPick: () => request({ url: '/book/blind-pick' })
}
