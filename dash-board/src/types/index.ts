export interface PageResponse<T> {
  page: Page
  content: T[]
}

export interface Page {
  size: number
  number: number
  totalElements: number
  totalPages: number
}

export interface ValidationError {
  status: number
  error: string
  message: string
  timestmap: string
  errors: {
    field: string
    message: string
  }[]
}

export interface ImageDTO {
  id: number
  src: string
  alt: string
  position: number
}
