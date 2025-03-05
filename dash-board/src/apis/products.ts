import { type TypeSchema as ProductSchema } from '@/components/page/product/form/schema'
import type { PageResponse } from '@/types'
import type { ImageData } from '@/types/products/reqTypes'
import type { Product, ProductCode, ProductFull } from '@/types/products/resTypes'
import { fetchDeleteMulti, type FormDataItem } from './utils'

export async function fetchProducts(): Promise<PageResponse<Product>> {
  return fetchResouceSimple<PageResponse<Product>>(
    `/api/admin/products${window.location.search.toString()}`,
  )
}

export async function fetchProductsList(ids: string[]): Promise<Product[]> {
  const queryString = ids.map((id) => `ids=${id}`).join('&')
  return fetchResouceSimple<Product[]>('/api/admin/products/list?' + queryString)
}

export async function fetchProductsListToMap(ids: string[]): Promise<Map<Product['id'], Product>> {
  const products = await fetchProductsList(ids)
  const map = new Map<string, Product>()
  products.forEach((p) => {
    map.set(p.id, p)
  })

  return map
}

export const fetchProductCode = () => fetchResouceSimple<ProductCode[]>('/api/admin/product-codes')

export async function fetchProduct(id: string): Promise<ProductFull> {
  return fetchResouceSimple<ProductFull>(`/api/admin/products/${id}`)
}

export function fetchCreateFullProduct(productUpdateFull: ProductSchema): Promise<Product> {
  const formItems: FormDataItem[] = []
  const thumbnails: File[] | undefined = productUpdateFull.thumbnails

  const imageData: ImageData = {
    createThumbnails: productUpdateFull.imageData?.thumbnails?.map((t, index) => ({
      position: index + 1,
    })),
  }

  if (thumbnails && imageData.createThumbnails?.length) {
    formItems.push({ key: 'thumbnails', value: thumbnails, type: 'IMAGE' })
    formItems.push({ key: 'imageData', value: imageData, type: 'JSON' })
  }

  const mainImage: File = productUpdateFull.mainImage
  if (mainImage) {
    formItems.push({ key: 'mainImage', value: mainImage, type: 'IMAGE' })
  }

  delete productUpdateFull.mainImage
  delete productUpdateFull.thumbnails
  delete productUpdateFull.imageData
  formItems.push({ key: 'product', value: productUpdateFull, type: 'JSON' })

  return fetchChangeFormData('/api/admin/products', formItems, 'POST')
}

export function fetchUpdateFullProduct(
  productUpdateFull: ProductSchema,
  id: number | string,
): Promise<Product> {
  const formItems: FormDataItem[] = []
  const thumbnails: File[] | undefined = productUpdateFull.thumbnails
  const imageData: ImageData = {
    createThumbnails: productUpdateFull.imageData?.thumbnails
      ?.filter((t) => !t.id)
      .map((t) => ({
        position: t.position,
      })),
    editThumbnails:
      productUpdateFull.imageData?.thumbnails
        ?.filter((t) => t.id)
        .map((t) => ({ id: t.id!, position: t.position })) || [],
  }

  if (!imageData.createThumbnails?.length) {
    delete imageData.createThumbnails
  }

  if (imageData.createThumbnails || imageData.editThumbnails) {
    formItems.push({ key: 'imageData', value: imageData, type: 'JSON' })
  }

  if (thumbnails && imageData.createThumbnails?.length) {
    formItems.push({ key: 'thumbnails', value: thumbnails, type: 'IMAGE' })
  }

  const mainImage: File = productUpdateFull.mainImage
  if (mainImage) {
    formItems.push({ key: 'mainImage', value: mainImage, type: 'IMAGE' })
  }

  delete productUpdateFull.mainImage
  delete productUpdateFull.thumbnails
  delete productUpdateFull.imageData
  formItems.push({ key: 'product', value: productUpdateFull, type: 'JSON' })

  return fetchChangeFormData('/api/admin/products/' + id, formItems, 'PUT')
}

export async function fetchProductDeleteById(id: string | number) {
  return fetchDeleteSimple('/api/admin/products', id)
}

export async function fetchProductDeleteByIdIn(ids: string[] | number[]) {
  return fetchDeleteMulti(`/api/admin/products`, ids)
}
