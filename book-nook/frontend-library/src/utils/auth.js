const TOKEN_KEY = 'book_nook_token'
const USER_KEY = 'book_nook_user'

export function getToken() {
  return localStorage.getItem(TOKEN_KEY)
}

export function setLogin(data) {
  localStorage.setItem(TOKEN_KEY, data.token)
  localStorage.setItem(USER_KEY, JSON.stringify(data))
}

export function getUser() {
  try {
    return JSON.parse(localStorage.getItem(USER_KEY) || '{}')
  } catch {
    return {}
  }
}

export function logout() {
  localStorage.removeItem(TOKEN_KEY)
  localStorage.removeItem(USER_KEY)
}
