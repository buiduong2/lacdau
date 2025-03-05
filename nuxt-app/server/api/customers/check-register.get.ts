import { getAuthHeaders } from '~/server/utils/auth/oauth-helpers'

export default defineEventHandler(async event => {
	const accessToken: string | undefined = await AuthUtils.getAccessToken(
		event
	)

	if (accessToken) {
		try {
			const result = await $fetch<boolean>(
				`${RESOURCE_URL}/api/customers/check-register`,
				{
					headers: getAuthHeaders(accessToken)
				}
			)

			setCookie(event, 'customer-registered', JSON.stringify(result), {
				httpOnly: true,
				secure: true,
				sameSite: 'lax',
				path: '/'
			})
			return result
		} catch (error) {
			console.log(error)
		}
	}
	AuthUtils.logout(event)
	throw createError({ status: 401, message: 'Unauthorized' })
})
