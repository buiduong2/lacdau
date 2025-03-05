export interface TokenRes {
  access_token: string
  refresh_token: string
  scope: 'DASHBOARD'
  token_type: 'Bearer'
  expires_in: number
  id_token: string
}

export interface AccessTokenBody {
  sub: string
  aut: string
  nbf: number
  scope: string[]
  authorities: (keyof typeof Authority)[]
  iss: string
  exp: number
  iat: number
  jti: string
  userId: number
}

export interface Profile {
  id: number
  displayName: string
  email?: string
  avatarUrl: string
}

export enum Authority {
  PRODUCT_MANAGE = 'PRODUCT_MANAGE',
  ORDER_MANAGE = 'ORDER_MANAGE',
  CUSTOMER_MANAGE = 'CUSTOMER_MANAGE',
  USER_MANAGE = 'USER_MANAGE',
  REPORT_VIEW = 'REPORT_VIEW',
}
