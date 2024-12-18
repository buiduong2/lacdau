import type { AttributeValueUpdate } from '@/types/attribute-values/reqTypes'
import type { AttributeValue, AttributeValueBasic } from '@/types/attribute-values/resTypes'

export const fetchAttributeValues = () => fetchResouceSimple<AttributeValue[]>('/attribute-values')

export async function fetchAttributeValueCreate(
  data: AttributeValueUpdate,
): Promise<AttributeValueBasic> {
  return fetchChangeSimple<AttributeValueUpdate, AttributeValueBasic>(
    'attribute-values',
    data,
    'POST',
  )
}

export async function fetchAttributeValueEdit(data: AttributeValueUpdate, id: number) {
  return fetchChangeSimple<AttributeValueUpdate, AttributeValueBasic>(
    'attribute-values/' + id,
    data,
    'PUT',
  )
}

export async function fetchAttributeValueDelete(id: number) {
  return fetchDeleteSimple('attribute-values', id)
}

export async function fetchAttributeValue(id: number): Promise<AttributeValueBasic> {
  return fetchResouceSimple('/attribute-values/' + id)
}
