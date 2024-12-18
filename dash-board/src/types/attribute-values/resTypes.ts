export interface AttributeValue {
  id: number
  name: string
  attributeId: number
  createdAt: string
  updatedAt: string
  productCount: number
}

export interface AttributeValueBasic {
  id: number
  name: string
  createdAt: string
  updatedAt: string
  attributeId: number
}
