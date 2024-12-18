export interface Category {
  id: number
  name: string
  imageSrc: string | null
  level: 0 | 1 | 2
  parentId: number | null
  createdAt: string
  updatedAt: string
}
