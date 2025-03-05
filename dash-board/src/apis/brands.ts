import type { BrandUpdate } from '@/types/brands/reqTypes'
import type { Brand, BrandBasic } from '@/types/brands/resTypes'

export const fetchBrands = () => fetchResouceSimple<Brand[]>('/api/admin/brands')

export async function fetchBrandCreate(brand: BrandUpdate): Promise<BrandBasic> {
  return fetchChangeSimple<BrandUpdate, BrandBasic>('/api/admin/brands', brand, 'POST')
}

export async function fetchBrand(id: number): Promise<BrandBasic> {
  return fetchResouceSimple('/api/admin/brands/' + id)
}

export async function fetchBrandEdit(brand: BrandUpdate, id: number): Promise<BrandBasic> {
  return fetchChangeSimple<BrandUpdate, BrandBasic>('/api/admin/brands/' + id, brand, 'PUT')
}

export async function fetchBrandDelete(id: number): Promise<void> {
  return fetchDeleteSimple('/api/admin/brands', id)
}
