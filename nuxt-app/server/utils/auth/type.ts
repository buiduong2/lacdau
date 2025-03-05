export type TokenId = string
export type SessionId = string

export type MayFail<T> = Promise<T>

export interface TokenRes {
	access_token: string
	refresh_token: string
	scope: 'CLIENT'
	token_type: 'Bearer'
	expires_in: number
	id_token: string
}

export type TokenSuplier = (refreshToken: string) => Promise<TokenRes>
export type TokenRevoker = (refreshToken: string) => Promise<void>

export interface Entity {
	setId(id: string): void
	getId(): string
}

export interface Token extends Entity {
	revoke(): MayFail<void>
	getByType(type: 'id' | 'refresh' | 'access'): MayFail<string>
	getRefreshToken(): MayFail<string>
	getIdToken(): MayFail<string>
	getAccessToken(): MayFail<string>
}

export type SessionData = Record<string, string>

export interface Session<T extends SessionData = SessionData> extends Entity {
	getId(): string
	getData(): T
	setData(data: T): void
}

export interface TokenManager {
	get(tokenId: TokenId): Token | undefined
	save(tokenRes: TokenRes): Token
	delete(tokenId: TokenId): void
}

export interface UserDetails extends Entity {
	authorities: string[]
	userId: number
	getAuthorities(): string[]
	getUserId(): number
}

export interface SessionManager<T extends SessionData> {
	get(sessionId: string): Session<T> | undefined

	createSession(data: T): Session<T>

	delete(sessionId: string): void
}

export interface AuthenticationManager {
	getAccessToken(sessionId: string): Promise<string | undefined>

	getIdToken(sessionId: string): Promise<string | undefined>

	login(token: TokenRes): SessionId

	authenticate(sessionId: string): UserDetails | undefined

	logout(sessionId: string): Promise<void>
}

export interface AccessTokenBody {
	sub: string
	aut: string
	nbf: number
	scope: string[]
	authorities: string[]
	iss: string
	exp: number
	iat: number
	jti: string
	userId: number
}
