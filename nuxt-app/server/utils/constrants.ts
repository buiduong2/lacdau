export const oAuthConfig = {
	CLIENT_ID: process.env.NUXT_PUBLIC_AUTH_CLIENT_ID || '',
	SCOPE: process.env.NUXT_PUBLIC_AUTH_SCOPE || '',
	LOGIN_CALLBACK: process.env.NUXT_PUBLIC_AUTH_LOGIN_CALLBACK || '',
	code_challenge_method: 'S256',
	AUTHORIZE_URL: process.env.NUXT_PUBLIC_AUTH_AUTHORIZE_URL || '',
	SECRET: process.env.SERVER_AUTH_SECRET || '',
    LOGOUT_CALLBACK: process.env.SERVER_AUTH_LOGOUT_CALLBACK || ''
}

export const RESOURCE_URL: string = process.env.PUBLIC_RESOURCE_API_URL || ''


export const AUTH_URL = process.env.PUBLIC_AUTH_API_URL || ''

