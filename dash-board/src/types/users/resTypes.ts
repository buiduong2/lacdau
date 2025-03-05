interface BaseUser {
  id: number
  displayName: string
  avatarUrl?: string
  emailVerified: boolean
  moderate: boolean
  userType: string
  updatedAt: string
  createdAt: string
}

interface SystemUser extends BaseUser {
  username: string
  password?: string
  email: string
  userType: 'SYS'
}

interface OAuthUser extends BaseUser {
  providerUserId: string
  username: string
  email: string
  provider: 'GITHUB' | 'GOOGLE'
  userType: 'OAUTH'
}

export type User = OAuthUser | SystemUser
