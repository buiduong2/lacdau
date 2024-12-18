import { resourceUrl } from '~/utils/constant'
import { CategoryRes } from '~/utils/typesFetch'

export default defineEventHandler(event => {
	return $fetch(`${resourceUrl}/api/categories`) as Promise<CategoryRes[]>
})
