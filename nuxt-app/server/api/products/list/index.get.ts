import { RESOURCE_URL } from '~/server/utils/constrants'
import { ProductSummaryRes } from '~/utils/typesFetch'

export default defineEventHandler(event => {
	const query = getQuery(event)

	return $fetch(`${RESOURCE_URL}/api/products/list`, {
		query: { ids: query.ids }
	}) as Promise<ProductSummaryRes[]>
})
