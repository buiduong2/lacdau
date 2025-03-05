import { RESOURCE_URL } from '~/server/utils/constrants'
import { FilterRes } from '~/utils/typesFetch'

export default defineEventHandler(event => {
	const categoryId: string = getRouterParams(event).categoryId

	return $fetch(
		`${RESOURCE_URL}/api/products/filter/${categoryId}`
	) as Promise<FilterRes>
})
