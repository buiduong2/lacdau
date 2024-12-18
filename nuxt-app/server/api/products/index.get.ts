import { resourceUrl } from '~/utils/constant'
import { Page, ProductSummaryRes } from '~/utils/typesFetch'

export default defineEventHandler(event => {
	const query = getQuery(event)
	
	return $fetch(`${resourceUrl}/api/products`, {
		query
	}) as Promise<Page<ProductSummaryRes>>
})
