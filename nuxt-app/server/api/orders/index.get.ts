import { OrderSummaryRes, Page } from '~/utils/typesFetch'

export default defineEventHandler<
	{ query: { page: number; type: string } },
	Promise<Page<OrderSummaryRes>>
>(async event => {
	const accessToken: string | undefined = await AuthUtils.getAccessToken(
		event
	)

	if (accessToken) {
		const query = getQuery(event)
		return $fetch(`${RESOURCE_URL}/api/order`, {
			headers: getAuthHeaders(accessToken),
			query: {
				page: query.page,
				size: 5,
				status: query.type
			}
		})
	}
	AuthUtils.logout(event)
	throw createError({ status: 401, message: 'Unauthorized' })
})
