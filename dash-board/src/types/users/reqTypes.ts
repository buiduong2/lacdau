import type { Schema } from '@/components/page/users/form/schema'

export type UserCreate = Schema

export type UserEdit = {
  displayName: string
  email?: string
}

export type UserEditPassword = {
  password: string
  confirmPassword: string
}
