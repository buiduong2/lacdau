import type { AttributeValueUpdate } from '@/types/attribute-values/reqTypes'
import type { AttributeValue, AttributeValueBasic } from '@/types/attribute-values/resTypes'

export const fetchAttributeValues = () =>
  fetchResouceSimple<AttributeValue[]>('/api/admin/attribute-values')

export async function fetchAttributeValueCreate(
  data: AttributeValueUpdate,
): Promise<AttributeValueBasic> {
  return fetchChangeSimple<AttributeValueUpdate, AttributeValueBasic>(
    '/api/admin/attribute-values',
    data,
    'POST',
  )
}

export async function fetchAttributeValueEdit(data: AttributeValueUpdate, id: number) {
  return fetchChangeSimple<AttributeValueUpdate, AttributeValueBasic>(
    '/api/admin/attribute-values/' + id,
    data,
    'PUT',
  )
}

export async function fetchAttributeValueDelete(id: number) {
  return fetchDeleteSimple('/api/admin/attribute-values', id)
}

export async function fetchAttributeValue(id: number): Promise<AttributeValueBasic> {
  return fetchResouceSimple('/api/admin/attribute-values/' + id)
}
