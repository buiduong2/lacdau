import type { ProductStatus } from './resTypes'

export interface ProductUpdate {
  name: string
  originalPrice: number
  salePrice?: number | null
  quantity: number

  mainImage?: {
    src: string
    alt: string
  } | null
}

export interface ProductUpdateFull {
  name: string
  prefixProductCode: string
  originalPrice: number
  salePrice?: number | null
  quantity: number
  status: ProductStatus
  detail: {
    specifications: string[]
  }

  relateInfo?: {
    name: string
    relateGroupId: number
  }

  attributeValueIds?: number[]
  categoryId?: number
  brandId?: number
}

export interface ImageData {
  createThumbnails?: { position: number }[]
  editThumbnails?: { id: number; position: number }[]
}
