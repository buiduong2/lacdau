export function calculateExpirationDate(expiresIn: number): Date {
	const now = new Date()
	const expirationDate = new Date(now.getTime() + expiresIn * 1000)
	return expirationDate
}
export function parseAccessToken(accessToken: string): AccessTokenBody {
	return JSON.parse(atob(accessToken.split('.')[1]))
}
