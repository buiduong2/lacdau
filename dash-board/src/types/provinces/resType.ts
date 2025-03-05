export interface ProvinceBasic {
  id: number
  name: string
}

export interface Province {
  id: number
  name: string
  districts: {
    id: number
    name: string
  }[]
}
