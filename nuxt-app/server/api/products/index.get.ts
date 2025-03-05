import { RESOURCE_URL } from '~/server/utils/constrants'
import { Page, ProductSummaryRes } from '~/utils/typesFetch'

export default defineEventHandler(event => {
	const query = getQuery(event)
	
	return $fetch(`${RESOURCE_URL}/api/products`, {
		query
	}) as Promise<Page<ProductSummaryRes>>
})
