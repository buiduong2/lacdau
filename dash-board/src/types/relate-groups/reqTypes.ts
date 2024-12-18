export interface RelateGroupCreate {
  name: string
  relateInfos?: RelateInfoUpdate[]
}

export interface RelateInfoUpdate {
  productId: string
  name: string
  position: number
}

export interface ProductRelateInfo {
  id: string
  name: string
  originalPrice: number
  salePrice?: number
  quantity: number
  mainImage?: {
    id: number
    src: string
    alt: string
  }
  relateInfo?: {
    id: number
    name: string
    relateGroupId: number
  }
}
