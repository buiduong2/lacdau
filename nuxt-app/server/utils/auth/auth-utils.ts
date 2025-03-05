import { AuthenticationManagerImpl } from './authentication-manager'
import type {
	AuthenticationManager,
	SessionId,
	TokenRes,
	UserDetails
} from './type'

type RequestEvent = Parameters<typeof setCookie>[0]

export class AuthUtils {
	private static readonly delegate: AuthenticationManager =
		new AuthenticationManagerImpl()

	public static async authenticate(sessionId: string) {
		return this.delegate.authenticate(sessionId)
	}

	public static async logout(event: RequestEvent) {
		const sessionId = this.getAuthCookie(event)
		this.removeAuthCookie(event)
		if (sessionId) {
			await this.delegate.logout(sessionId)
		}
	}

	public static async getAccessToken(
		event: RequestEvent
	): Promise<string | undefined> {
		const sessionId = this.getAuthCookie(event)
		if (sessionId) {
			const accessToken = this.delegate.getAccessToken(sessionId)
			if (accessToken) {
				return accessToken
			} else {
				this.removeAuthCookie(event)
			}
		}
		return
	}

	public static async getIdToken(
		event: RequestEvent
	): Promise<string | undefined> {
		const sessionId = this.getAuthCookie(event)
		if (sessionId) {
			const idToken = await this.delegate.getIdToken(sessionId)
			if (idToken) {
				return idToken
			} else {
				this.removeAuthCookie(event)
			}
		}
		return
	}

	public static async  login(event: RequestEvent, tokenRes: TokenRes): Promise<UserDetails> {
		const sessionId = this.delegate.login(tokenRes)
		this.setAuthCookie(event, sessionId)
		return (await this.authenticate(sessionId))!
	}

	private static getAuthCookie(event: RequestEvent): string | undefined {
		return getCookie(event, 'auth')
	}

	private static removeAuthCookie(event: RequestEvent) {
		const past = new Date(Date.now() - 1000)

		setCookie(event, 'auth', 'expired', {
			httpOnly: true,
			sameSite: 'lax',
			expires: past
		})
	}

	private static setAuthCookie(
		event: RequestEvent,
		sessionId: SessionId
	): void {
		setCookie(event, 'auth', sessionId, { httpOnly: true, sameSite: 'lax' })
	}
}
