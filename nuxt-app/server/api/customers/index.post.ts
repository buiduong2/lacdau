import { getAuthHeaders } from '~/server/utils/auth/oauth-helpers'
import { CustomerRegisterReq, type Customer } from '~/utils/typesFetch'

export default defineEventHandler<
	{ body: CustomerRegisterReq },
	Promise<Customer>
>(async event => {
	const accessToken: string | undefined = await AuthUtils.getAccessToken(
		event
	)

	if (accessToken) {
		try {
			const body = await readBody<CustomerRegisterReq>(event)
			return await $fetch<Customer>(`${RESOURCE_URL}/api/customers`, {
				method: 'POST',
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
