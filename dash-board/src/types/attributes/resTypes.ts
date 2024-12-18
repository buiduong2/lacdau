export interface Attribute {
  id: number
  name: string
  createdAt: string
  updatedAt: string
  attributeValueCount: number
}

export interface AttributeBasic {
  id: number
  name: string
  createdAt: string
  updatedAt: string

  attributeValueIds: number[]
}
