import type { Category } from '@/types/categories/resTypes'
import { type TypeSchema as CategorySChema } from '@/components/page/categories/form/schema'
import type { FormDataItem } from './utils'

export const fetchCategories = () => fetchResouceSimple<Category[]>('/api/admin/categories')
export async function fetchCategory(id: number): Promise<Category> {
  return fetchResouceSimple('/api/admin/categories/' + id)
}

export async function fetchCategoryCreate(data: CategorySChema) {
  const image = data.image
  delete data.image
  const formItems: FormDataItem[] = []

  if (image) {
    formItems.push({ key: 'image', value: image, type: 'IMAGE' })
  }

  formItems.push({ key: 'category', value: data, type: 'JSON' })

  return fetchChangeFormData('/api/admin/categories', formItems, 'POST')
}

export async function fetchCategoryEdit(data: CategorySChema, id: number) {
  const image = data.image
  delete data.image
  const formItems: FormDataItem[] = []

  if (image) {
    formItems.push({ key: 'image', value: image, type: 'IMAGE' })
  }

  formItems.push({ key: 'category', value: data, type: 'JSON' })

  return fetchChangeFormData('/api/admin/categories/' + id, formItems, 'PUT')
}

export async function fetchCategoryDelete(id: number) {
  return fetchDeleteSimple('/api/admin/categories', id)
}
