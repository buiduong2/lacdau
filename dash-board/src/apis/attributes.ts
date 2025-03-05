import type { AttributeCreate, AttributeUpdate } from '@/types/attributes/reqTypes'
import type { Attribute, AttributeBasic } from '@/types/attributes/resTypes'

export const fetchAttributes = () => fetchResouceSimple<Attribute[]>('/api/admin/attributes')

export function fetchAttribute(id: number | string): Promise<AttributeBasic> {
  return fetchResouceSimple('/api/admin//attributes/' + id)
}

export async function fetchAttributeCreate(data: AttributeCreate) {
  return fetchChangeSimple<AttributeCreate, AttributeBasic>('/api/admin/attributes', data, 'POST')
}

export async function fetchAttributeUpdate(data: AttributeUpdate, id: number | string) {
  return fetchChangeSimple<AttributeUpdate, AttributeBasic>(
    '/api/admin/attributes/' + id,
    data,
    'PUT',
  )
}

export function fetchAttributeDelete(id: number | string) {
  return fetchDeleteSimple('/api/admin/attributes', id)
}
