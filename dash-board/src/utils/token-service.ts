import type { TokenRes } from '@/types/auth/resTypes'

export class TokenService {
  private accessToken: string
  private refreshToken: string
  private idToken: string
  private isRefeshing = false
  private pendingRequests: ((tokenRes?: TokenRes) => void)[]
  private expDate: Date
  private tokenSuplier: (refreshToken: string) => Promise<TokenRes>

  private static instance: TokenService | null = null

  private constructor(
    accessToken: string,
    refreshToken: string,
    idToken: string,
    expDate: Date,
    tokenSuplier: (refreshToken: string) => Promise<TokenRes>,
  ) {
    this.accessToken = accessToken
    this.refreshToken = refreshToken
    this.expDate = expDate
    this.pendingRequests = []
    this.idToken = idToken
    this.tokenSuplier = tokenSuplier
  }

  public static getInstance(): TokenService {
    if (this.instance) {
      return this.instance
    }
    throw new Error('You Need create Instance before access It')
  }

  public static createInstance(
    { access_token, refresh_token, id_token, expires_in }: TokenRes,
    tokenSuplier: (refreshToken: string) => Promise<TokenRes>,
  ): TokenService {
    this.instance = new TokenService(
      access_token,
      refresh_token,
      id_token,
      calculateExpirationDate(expires_in),
      tokenSuplier,
    )
    return this.instance
  }

  public async getRefreshToken(): Promise<string> {
    return this.refreshToken
  }

  public async getIdToken(): Promise<string> {
    if (this.expDate > new Date()) {
      return this.idToken
    }
    const tokenRes: TokenRes = await this.refreshTokenIfNeeded()
    return tokenRes.id_token
  }

  public async getAccessToken(): Promise<string> {
    if (this.expDate > new Date()) {
      return this.accessToken
    }

    const tokenRes: TokenRes = await this.refreshTokenIfNeeded()
    return tokenRes.access_token
  }

  private async refreshTokenIfNeeded(): Promise<TokenRes> {
    if (this.isRefeshing) {
      return new Promise((resolve, reject) => {
        this.pendingRequests.push((token) => {
          if (token) {
            resolve(token)
          } else {
            reject(new Error('Failed to refresh Token'))
          }
        })
      })
    }

    this.isRefeshing = true

    try {
      const tokenRes: TokenRes = await this.tokenSuplier(this.refreshToken)
      this.updateToken(tokenRes)
      this.proccessPendingRequests(tokenRes)
      return tokenRes
    } catch (error) {
      this.proccessPendingRequests(undefined)
      throw error
    } finally {
      this.isRefeshing = false
    }
  }

  private proccessPendingRequests(token?: TokenRes) {
    this.pendingRequests.forEach((callback) => callback(token))
    this.pendingRequests = []
  }

  private updateToken(tokenRes: TokenRes): void {
    this.accessToken = tokenRes.access_token
    this.refreshToken = tokenRes.refresh_token
    this.idToken = tokenRes.id_token
    this.expDate = calculateExpirationDate(tokenRes.expires_in)
  }
}
