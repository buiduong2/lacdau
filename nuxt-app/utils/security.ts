function openOAuthPopup(url: string): ReturnType<Window['open']> {
	return window.open(url, 'OAuth2', 'width=500,height=600,top=100,left=200')
}

export function changePasswordWithPopup() {
	const url: string = generateOAuthChangePasswordUrl()
	openOAuthPopup(url)
}

export function registerWithPopup() {
	const url: string = generateOAuthRegisterUrl()
	openOAuthPopup(url)
}

export function authInfoWithPopup() {
	const url: string = generateOAuthProfileUrl()
	openOAuthPopup(url)
}

export async function loginWithPopUp(): Promise<{
	userId: number
	authorities: string[]
}> {
	return new Promise(async (res, rej) => {
		const { challenge, verifier } = await generatePKCE()
		const oauthUrl = generateOAuthUrl(challenge)
		const authWindow = openOAuthPopup(oauthUrl)

		const interval = setInterval(async () => {
			if (authWindow?.closed) {
				clearInterval(interval)
				try {
					let params = new URL(authWindow.location.toString())
						.searchParams
					const code = params.get('code')
					if (code) {
						const tokenRes = await $fetch(
							'/api/oauth2/access-token',
							{
								method: 'POST',
								body: { code, verifier }
							}
						)

						res(tokenRes)
					} else {
						rej('cancel')
					}
				} catch (error) {
					rej('cancel')
				}
			}
		}, 500)
	})
}

function generateOAuthChangePasswordUrl(): string {
	const { authUrl } = useRuntimeConfig().public
	return `${authUrl}/change-password`
}

function generateOAuthRegisterUrl(): string {
	const { authUrl } = useRuntimeConfig().public

	return `${authUrl}/register`
}

function generateOAuthProfileUrl(): string {
	const { authUrl } = useRuntimeConfig().public

	return `${authUrl}/profile`
}

export function generateOAuthUrl(challenge: string): string {
	const {
		authorizeUrl,
		client_id,
		code_challenge_method,
		redirect_uri,
		scope
	} = useRuntimeConfig().public
	return `${authorizeUrl}?response_type=code&client_id=${client_id}&scope=${scope}&redirect_uri=${redirect_uri}&code_challenge=${challenge}&code_challenge_method=${code_challenge_method}`
}

// GENERATING CODE VERIFIER
function dec2hex(dec: any) {
	return ('0' + dec.toString(16)).substr(-2)
}

function sha256(plain: any) {
	// returns promise ArrayBuffer
	const encoder = new TextEncoder()
	const data = encoder.encode(plain)
	return window.crypto.subtle.digest('SHA-256', data)
}

function base64urlencode(a: any) {
	var str = ''
	var bytes = new Uint8Array(a)
	var len = bytes.byteLength
	for (var i = 0; i < len; i++) {
		str += String.fromCharCode(bytes[i])
	}
	return btoa(str).replace(/\+/g, '-').replace(/\//g, '_').replace(/=+$/, '')
}

function generateCodeVerifier() {
	var array = new Uint32Array(56 / 2)
	window.crypto.getRandomValues(array)
	return Array.from(array, dec2hex).join('')
}

async function generateCodeChallengeFromVerifier(v: any) {
	var hashed = await sha256(v)
	var base64encoded = base64urlencode(hashed)
	return base64encoded
}

export async function generatePKCE(): Promise<{
	verifier: string
	challenge: string
}> {
	const verifier: string = generateCodeVerifier()
	const challenge: string = await generateCodeChallengeFromVerifier(verifier)
	return {
		challenge,
		verifier
	}
}

interface AccessTokenBody {
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

export function parseAccessToken(accessToken: string): AccessTokenBody {
	return JSON.parse(atob(accessToken.split('.')[1])) as AccessTokenBody
}
