import { AUTH_URL, oAuthConfig } from '../constrants'

const CLIENT_ID = oAuthConfig.CLIENT_ID
const SECRET = oAuthConfig.SECRET
const LOGOUT_CALLBACK = oAuthConfig.LOGOUT_CALLBACK
const LOGIN_CALLBACK = oAuthConfig.LOGIN_CALLBACK

export function getClientHeaders(): Record<string, string> {
	const clientCredential = btoa(`${CLIENT_ID}:${SECRET}`)
	return {
		Authorization: 'Basic ' + clientCredential
	}
}

export function getAuthHeaders(accessToken: string): Record<string, string> {
	return {
		Authorization: 'Bearer ' + accessToken
	}
}

export function formDataForAccessToken(data: {
	code: string
	verifier: string
}): FormData {
	const formData = new FormData()
	formData.append('code', data.code)
	formData.append('grant_type', 'authorization_code')

	formData.append('redirect_uri', LOGIN_CALLBACK)
	formData.append('code_verifier', data.verifier)
	return formData
}

export function formDataForRefreshToken(data: {
	refreshToken: string
}): FormData {
	const formData = new FormData()
	formData.append('grant_type', 'refresh_token')
	formData.append('refresh_token', data.refreshToken)
	return formData
}

export function formDataForRevokeToken(data: {
	refreshToken: string
}): FormData {
	const formData = new FormData()
	formData.append('token', data.refreshToken)
	return formData
}

export function generateOauthLogoutUrl(idToken: string) {
	return (
		AUTH_URL +
		'/connect/logout?id_token_hint=' +
		idToken +
		'&post_logout_redirect_uri=' +
		LOGOUT_CALLBACK
	)
}
