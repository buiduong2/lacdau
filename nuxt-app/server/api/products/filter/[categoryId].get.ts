import { resourceUrl } from '~/utils/constant'
import { FilterRes } from '~/utils/typesFetch'

export default defineEventHandler(event => {
	const categoryId: string = getRouterParams(event).categoryId

	return $fetch(
		`${resourceUrl}/api/products/filter/${categoryId}`
	) as Promise<FilterRes>
})
