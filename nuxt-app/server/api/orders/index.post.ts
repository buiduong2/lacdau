import { OrderCreateReq, OrderDetailRes } from '~/utils/typesFetch'

export default defineEventHandler<
	{ body: OrderCreateReq },
	Promise<OrderDetailRes>
>(async event => {
	const accessToken: string | undefined = await AuthUtils.getAccessToken(
		event
	)

	if (accessToken) {
		try {
			const body = await readBody(event)
			return await $fetch(`${RESOURCE_URL}/api/order`, {
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
