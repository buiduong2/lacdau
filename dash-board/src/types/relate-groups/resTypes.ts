export interface RelateGroup {
  id: number
  name: string
  createdAt: string
  updatedAt: string
  relateInfoCount: number
}

export interface RelateGroupInfo {
  id: number
  name: string
  createdAt: string
  updatedAt: string
  relateInfos: {
    id: number
    productId: string
    name: string
    price: number
  }[]
}

export interface RelateGroupBasic {
  id: number
  name: string
  createdAt: string
  updatedAt: string
}
