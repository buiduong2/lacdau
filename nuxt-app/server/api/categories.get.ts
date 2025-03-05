import { CategoryRes } from '~/utils/typesFetch'
import { RESOURCE_URL } from '../utils/constrants'

export default defineEventHandler(event => {
	return $fetch(`${RESOURCE_URL}/api/categories`) as Promise<CategoryRes[]>
})
