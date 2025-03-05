import { formDataForRefreshToken, getClientHeaders } from './oauth-helpers'
import type { TokenRes, TokenRevoker, TokenSuplier } from './type'

const tokenSuplier: TokenSuplier = async (refreshToken: string) => {
	return await $fetch<TokenRes>(`${AUTH_URL}/oauth2/token`, {
		method: 'POST',
		body: formDataForRefreshToken({ refreshToken: refreshToken }),
		headers: getClientHeaders()
	})
}

const tokenRevoker: TokenRevoker = async (refreshToken: string) => {
	return await $fetch(`${AUTH_URL}/oauth2/revoke`, {
		method: 'POST',
		body: formDataForRefreshToken({ refreshToken: refreshToken }),
		headers: getClientHeaders()
	})
}

export const OAUTH = {
	tokenSuplier,
	tokenRevoker
}
