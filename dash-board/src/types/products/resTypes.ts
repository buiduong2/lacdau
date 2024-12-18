import type { ImageDTO } from '..'

export enum ProductStatus {
  ACTIVE = 'ACTIVE',
  ARCHIVED = 'ARCHIVED',
  DRAFT = 'DRAFT',
}

export interface Product {
  id: string
  name: string
  originalPrice: number
  salePrice?: number | null
  quantity: number
  mainImage: {
    id: number
    src: string
    alt: string
  } | null
  categoryId?: number
  brandId?: number
  relateGroupId?: number
  detailId?: number
  relateInfoId?: number
  createdAt: string
  updatedAt: string
  status: ProductStatus
}

export interface ProductFull {
  id: string
  name: string
  prefixProductCode: string
  originalPrice: number
  salePrice?: number | null
  quantity: number
  mainImage?: ImageDTO
  status: ProductStatus
  detail: {
    specifications: string[]
    thumbnails?: ImageDTO[]
  }

  relateInfo?: {
    name: string
    relateGroupId: number
  }

  attributeValueIds?: number[]
  categoryId?: number
  brandId?: number
}

export interface ProductCode {
  name: string
  counter: number
  desc: string
}
