export interface BrandBasic {
  id: number
  name: string
  createdAt: string
  updatedAt: string
}

export interface Brand extends BrandBasic {
  productCount: number
}

export interface BrandUpdate {
  name: string
}
