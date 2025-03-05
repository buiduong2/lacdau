import { getAuthHeaders } from '~/server/utils/auth/oauth-helpers'
import { type CustomerRegisterReq, type Customer } from '~/utils/typesFetch'

export default defineEventHandler(async event => {
	const accessToken: string | undefined = await AuthUtils.getAccessToken(
		event
	)

	if (accessToken) {
		try {
			const body = await readBody<CustomerRegisterReq>(event)
			return await $fetch<Customer>(`${RESOURCE_URL}/api/customers`, {
				method: 'PUT',
				headers: getAuthHeaders(accessToken),
				body: body
			})
		} catch (error) {
			console.log(error)
		}
	}
	AuthUtils.logout(event)
	throw createError({ status: 401, message: 'Unauthorized' })
})
