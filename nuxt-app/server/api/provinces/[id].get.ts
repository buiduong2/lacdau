import { Province } from '~/utils/typesFetch'

export default defineEventHandler(event => {
	const id: string = getRouterParams(event).id
	return $fetch(`${RESOURCE_URL}/api/provinces/${id}`) as Promise<Province>
})
