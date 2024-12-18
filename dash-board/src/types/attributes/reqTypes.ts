export interface AttributeCreate {
  name: string
  attributeValues?: { name: string }[]
}

export interface AttributeUpdate {
  name: string
  removeAttributeValueId?: number[]
  attributeValues?: { name: string }[]
}
