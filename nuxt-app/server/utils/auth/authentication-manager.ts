import type {
	AuthenticationManager,
	SessionManager,
	Token,
	TokenManager,
	TokenRes,
	UserDetails
} from './type'
import { InMemorySessionManager } from './session-manager'
import { InmemoryTokenManager } from './token-manager'
import { InmemoryUserDetailsManager } from './user-details-service'

type AuthData = {
	tokenId: string
	userDetailsId: string
}

export class AuthenticationManagerImpl implements AuthenticationManager {
	private readonly sessionManager: SessionManager<AuthData>
	private readonly tokenManager: TokenManager
	private readonly userDetailsManager: InmemoryUserDetailsManager

	public constructor() {
		this.sessionManager = new InMemorySessionManager()
		this.tokenManager = new InmemoryTokenManager()
		this.userDetailsManager = new InmemoryUserDetailsManager()
	}

	public async getAccessToken(
		sessionId: string
	): Promise<string | undefined> {
		return this.getGenericToken(sessionId, 'access')
	}

	public async getIdToken(sessionId: string): Promise<string | undefined> {
		return this.getGenericToken(sessionId, 'id')
	}

	private async getGenericToken(
		sessionId: string,
		type: 'access' | 'refresh' | 'id'
	): Promise<string | undefined> {
		const session = this.sessionManager.get(sessionId)
		if (session) {
			const token = this.tokenManager.get(session.getData().tokenId)
			if (token) {
				try {
					return await token.getByType(type)
				} catch (error) {}
			}
		}
		this.invalidSession(sessionId)

		return
	}

	private invalidSession(sessionId: string) {
		const session = this.sessionManager.get(sessionId)
		if (session) {
			this.sessionManager.delete(session.getId())
			this.tokenManager.delete(session.getData().tokenId)
			this.userDetailsManager.delete(session.getData().userDetailsId)
		}
	}

	login(tokenRes: TokenRes): string {
		const token: Token = this.tokenManager.save(tokenRes)
		const userDetails: UserDetails = this.userDetailsManager.save(tokenRes)
		const session = this.sessionManager.createSession({
			tokenId: token.getId(),
			userDetailsId: userDetails.getId()
		})
		return session.getId()
	}

	authenticate(sessionId: string): UserDetails | undefined {
		const userDetailsId = this.sessionManager
			.get(sessionId)
			?.getData().userDetailsId
		if (userDetailsId) {
			return this.userDetailsManager.get(userDetailsId)
		}
		return
	}

	async logout(sessionId: string): Promise<void> {
		const session = this.sessionManager.get(sessionId)
		if (session) {
			const { userDetailsId, tokenId } = session.getData()
			const token = this.tokenManager.get(tokenId)!
			this.tokenManager.delete(tokenId)
			this.userDetailsManager.delete(userDetailsId)

			try {
				await token.revoke()
			} catch (error) {}
		}
	}
}
