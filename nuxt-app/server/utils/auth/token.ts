import { calculateExpirationDate } from './token-helpers'
import type { MayFail, Token, TokenRes, TokenRevoker } from './type'

export class JwtToken implements Token {
	private id: string | undefined
	private accessToken: string
	private refreshToken: string
	private idToken: string
	private isRefeshing = false
	private pendingRequests: ((tokenRes?: TokenRes) => void)[]
	private expDate: Date
	private tokenSuplier: (refreshToken: string) => Promise<TokenRes>
	private tokenRevoker: (idToken: string) => Promise<void>

	public constructor(
		{ access_token, refresh_token, id_token, expires_in }: TokenRes,
		tokenSuplier: (refreshToken: string) => Promise<TokenRes>,
		tokenRevoker: (idToken: string) => Promise<void>
	) {
		this.accessToken = access_token
		this.refreshToken = refresh_token
		this.expDate = calculateExpirationDate(expires_in)
		this.pendingRequests = []
		this.idToken = id_token
		this.tokenSuplier = tokenSuplier
		this.tokenRevoker = tokenRevoker
	}

	public setId(id: string): void {
		this.id = id
	}

	public getId(): string {
		return this.id!
	}

	getByType(type: 'id' | 'refresh' | 'access'): MayFail<string> {
		switch (type) {
			case 'id':
				return this.getIdToken()
			case 'access':
				return this.getAccessToken()
			case 'refresh':
				return this.getRefreshToken()
		}
	}

	public async getRefreshToken(): MayFail<string> {
		return this.refreshToken
	}

	public async getIdToken(): MayFail<string> {
		if (this.expDate > new Date()) {
			return this.idToken
		}
		const tokenRes: TokenRes = await this.refreshTokenIfNeeded()
		return tokenRes.id_token
	}

	public async getAccessToken(): MayFail<string> {
		if (this.expDate > new Date()) {
			return this.accessToken
		}

		const tokenRes: TokenRes = await this.refreshTokenIfNeeded()
		return tokenRes.access_token
	}

	private async refreshTokenIfNeeded(): Promise<TokenRes> {
		if (this.isRefeshing) {
			return new Promise((resolve, reject) => {
				this.pendingRequests.push(token => {
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
			const tokenRes: TokenRes = await this.tokenSuplier(
				this.refreshToken
			)
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

	public async revoke(): Promise<void> {
		const refreshToken = await this.getRefreshToken()
		return this.tokenRevoker(refreshToken)
	}

	private proccessPendingRequests(token?: TokenRes) {
		this.pendingRequests.forEach(callback => callback(token))
		this.pendingRequests = []
	}

	private updateToken(tokenRes: TokenRes): void {
		this.accessToken = tokenRes.access_token
		this.refreshToken = tokenRes.refresh_token
		this.idToken = tokenRes.id_token
		this.expDate = calculateExpirationDate(tokenRes.expires_in)
	}
}
