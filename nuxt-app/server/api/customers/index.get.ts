import { getAuthHeaders } from '~/server/utils/auth/oauth-helpers'
import { type Customer } from '~/utils/typesFetch'

export default defineEventHandler<{}, Promise<Customer>>(async event => {
	const accessToken: string | undefined = await AuthUtils.getAccessToken(
		event
	)

	if (accessToken) {
		try {
			return await $fetch<Customer>(`${RESOURCE_URL}/api/customers`, {
				headers: getAuthHeaders(accessToken)
			})
		} catch (error) {
			console.log(error)
		}
	}
	AuthUtils.logout(event)
	throw createError({ status: 401, message: 'Unauthorized' })
})
