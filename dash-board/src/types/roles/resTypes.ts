import type { Authority } from '../auth/resTypes'
type UserPermissionBasic = {
  id: number
  displayName: string
  avatarUrl?: string
  email?: string
  createdAt: string
  updatedAt: string
}

type PermissionBaic = {
  id: number
  permission: Authority
  revokedAt?: string
  updatedAt: string
  createdAt: string
}

export type UserPermission = UserPermissionBasic & { permissions: PermissionBaic[] }

export type UserPermissionDetail = {
  id: number
  displayName: string
  provider: 'SYS' | 'GITHUB' | 'GOOGLE'
  email?: string
  avatarUrl?: string
  updatedAt: string
  createdAt: string
  permissions: PermissionDetail[]
}

export type PermissionDetail = {
  id: number
  permission: Authority
  revokedAt?: string
  createdAt: string
  updatedAt: string
  updatedBy?: { id: number; displayName: string }
  createdBy?: { id: number; displayName: string }
}
