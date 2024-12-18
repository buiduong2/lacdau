import type { AttributeCreate, AttributeUpdate } from '@/types/attributes/reqTypes'
import type { Attribute, AttributeBasic } from '@/types/attributes/resTypes'

export const fetchAttributes = () => fetchResouceSimple<Attribute[]>('/attributes')

export function fetchAttribute(id: number | string): Promise<AttributeBasic> {
  return fetchResouceSimple('/attributes/' + id)
}

export async function fetchAttributeCreate(data: AttributeCreate) {
  return fetchChangeSimple<AttributeCreate, AttributeBasic>('attributes', data, 'POST')
}

export async function fetchAttributeUpdate(data: AttributeUpdate, id: number | string) {
  return fetchChangeSimple<AttributeUpdate, AttributeBasic>('attributes/' + id, data, 'PUT')
}

export function fetchAttributeDelete(id: number | string) {
  return fetchDeleteSimple('attributes', id)
}
