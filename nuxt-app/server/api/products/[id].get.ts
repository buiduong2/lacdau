import { resourceUrl } from '~/utils/constant'
import { ProductDetailRes } from '~/utils/typesFetch'

export default defineEventHandler(event => {
	const id: string = getRouterParams(event).id
	return $fetch(
		`${resourceUrl}/api/products/${id}`
	) as Promise<ProductDetailRes>
})
