import type { UserPermissionUpdate } from '@/types/roles/reqTypes'
import type { UserPermission, UserPermissionDetail } from '@/types/roles/resTypes'

export async function fetchUserPermissions() {
  return await getAuthClient().get<UserPermission[]>('/api/admin/user-permissions')
}

export async function fetchUserPermission(id: number | string) {
  return await getAuthClient().get<UserPermissionDetail>('/api/admin/user-permissions/' + id)
}

export async function fetchUserpermissionUpdate(
  userId: number | string,
  data: UserPermissionUpdate,
) {
  return await getAuthClient().put<UserPermissionDetail>(
    '/api/admin/user-permissions/' + userId,
    data,
  )
}
