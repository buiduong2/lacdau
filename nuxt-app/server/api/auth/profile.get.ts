import { getAuthHeaders } from '~/server/utils/auth/oauth-helpers'
import { type Profile } from '~/utils/typesFetch'

export default defineEventHandler(async event => {
	const accessToken: string | undefined = await AuthUtils.getAccessToken(
		event
	)

	if (accessToken) {
		try {
			return await $fetch<Profile>(`${AUTH_URL}/api/profile`, {
				headers: getAuthHeaders(accessToken)
			})
		} catch (error) {
			console.log(error)
		}
	}
	AuthUtils.logout(event)
	throw createError({ status: 401, message: 'Unauthorized' })
})
