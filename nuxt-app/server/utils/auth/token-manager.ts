import { OAUTH } from './api'
import { InmemoryRepository } from './inmemory-repository'
import { JwtToken } from './token'
import type {
	Token,
	TokenId,
	TokenManager,
	TokenRes,
	TokenRevoker,
	TokenSuplier
} from './type'

export class InmemoryTokenManager implements TokenManager {
	private readonly repository: InmemoryRepository<Token>
	private readonly tokenSuplier: TokenSuplier
	private readonly tokenRevoker: TokenRevoker

	public constructor() {
		this.repository = new InmemoryRepository(100)
		this.tokenSuplier = OAUTH.tokenSuplier
		this.tokenRevoker = OAUTH.tokenRevoker
	}

	get(tokenId: TokenId): Token | undefined {
		return this.repository.get(tokenId)
	}

	save(tokenRes: TokenRes): Token {
		const token: Token = new JwtToken(
			tokenRes,
			this.tokenSuplier,
			this.tokenRevoker
		)
		return this.repository.save(token)
	}

	delete(tokenId: TokenId) {
		this.repository.delete(tokenId)
	}
}
