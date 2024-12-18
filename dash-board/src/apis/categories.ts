import type { Category } from '@/types/categories/resTypes'
import { type TypeSchema as CategorySChema } from '@/components/page/categories/form/schema'
import type { FormDataItem } from './utils'

export const fetchCategories = () => fetchResouceSimple<Category[]>('/categories')
export async function fetchCategory(id: number): Promise<Category> {
  return fetchResouceSimple('/categories/' + id)
}

export async function fetchCategoryCreate(data: CategorySChema) {
  const image = data.image
  delete data.image
  const formItems: FormDataItem[] = []

  if (image) {
    formItems.push({ key: 'image', value: image, type: 'IMAGE' })
  }

  formItems.push({ key: 'category', value: data, type: 'JSON' })

  return fetchChangeFormData('categories', formItems, 'POST')
}

export async function fetchCategoryEdit(data: CategorySChema, id: number) {
  const image = data.image
  delete data.image
  const formItems: FormDataItem[] = []

  if (image) {
    formItems.push({ key: 'image', value: image, type: 'IMAGE' })
  }

  formItems.push({ key: 'category', value: data, type: 'JSON' })

  return fetchChangeFormData('categories/' + id, formItems, 'PUT')
}

export async function fetchCategoryDelete(id: number) {
  return fetchDeleteSimple('categories', id)
}
