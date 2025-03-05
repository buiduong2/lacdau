import { parseAccessToken } from './token-helpers'
import { InmemoryRepository } from './inmemory-repository'
import type { TokenRes, UserDetails } from './type'

export class InmemoryUserDetailsManager {
	private readonly repositry: InmemoryRepository<JwtAuthentiction>

	constructor() {
		this.repositry = new InmemoryRepository()
	}

	public get(userDetailsId: string): UserDetails | undefined {
		return this.repositry.get(userDetailsId)
	}

	public save(tokenRes: TokenRes): UserDetails {
		const tokenBody = parseAccessToken(tokenRes.access_token)
		const userDetails = new JwtAuthentiction(
			tokenBody.authorities,
			tokenBody.userId
		)
		this.repositry.save(userDetails)
		return userDetails
	}

	public delete(userDetailsId: string) {
		this.repositry.delete(userDetailsId)
	}
}

class JwtAuthentiction implements UserDetails {
	public id: string | undefined
	public authorities: string[]
	public userId: number

	public constructor(authorities: string[], userId: number) {
		this.authorities = authorities
		this.userId = userId
	}

	getUserId(): number {
		return this.userId
	}

	getAuthorities(): string[] {
		return this.authorities
	}

	setId(id: string): void {
		this.id = id
	}
	getId(): string {
		return this.id!
	}
}
