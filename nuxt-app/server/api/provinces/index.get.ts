import { ProvinceBasic } from '~/utils/typesFetch'

export default defineEventHandler(() => {
	return $fetch(`${RESOURCE_URL}/api/provinces`) as Promise<ProvinceBasic[]>
})
