import type { Province, ProvinceBasic } from '@/types/provinces/resType'

export async function fetchProvinces() {
  return getResourceClient().get<ProvinceBasic[]>('/api/provinces')
}

export async function fetchProvince(id: number | string) {
  return getResourceClient().get<Province>('/api/provinces/' + id)
}
