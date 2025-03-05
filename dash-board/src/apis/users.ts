import type { PageResponse } from '@/types'
import type { UserCreate, UserEdit, UserEditPassword } from '@/types/users/reqTypes'
import type { User } from '@/types/users/resTypes'

export async function fetchUsers() {
  return getAuthClient().get<PageResponse<User>>(
    '/api/admin/users' + window.location.search.toString(),
  )
}

export function fetchUser(id: number | string) {
  return getAuthClient().get<User>('/api/admin/users/' + id)
}

export function fetchUserEdit(id: number | string, user: UserEdit) {
  return getAuthClient().put('/api/admin/users/' + id, user)
}

export function fetchUserChangePassword(id: number | string, user: UserEditPassword) {
  return getAuthClient().put('/api/admin/users/' + id + '/change-password', user)
}

export async function fetchUserCreate(user: UserCreate) {
  return getAuthClient().post('/api/admin/users', user)
}

export function fetchUserCheckUsername(username: string) {
  return getAuthClient().get<boolean>('/api/admin/users/check/username/' + username)
}

export function fetchUserCheckEmail(email: string) {
  return getAuthClient().get<boolean>('/api/admin/users/check/email/' + email)
}
