import { RESOURCE_URL } from '~/server/utils/constrants'
import { ProductDetailRes } from '~/utils/typesFetch'

export default defineEventHandler(event => {
	const id: string = getRouterParams(event).id
	return $fetch(
		`${RESOURCE_URL}/api/products/${id}`
	) as Promise<ProductDetailRes>
})
