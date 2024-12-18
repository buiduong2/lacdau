import { type TypeSchema as ProductSchema } from '@/components/page/product/form/schema'
import type { PageResponse } from '@/types'
import type { ImageData } from '@/types/products/reqTypes'
import type { Product, ProductCode, ProductFull } from '@/types/products/resTypes'
import { resourceURL, type FormDataItem } from './utils'

export async function fetchProducts(): Promise<PageResponse<Product>> {
  const res = await fetch(`${resourceURL}/products${window.location.search.toString()}`, {})

  if (res.ok) {
    return (await res.json()) as PageResponse<Product>
  }

  throw new Error('Fetch Failed')
}

export const fetchProductCode = () => fetchResouceSimple<ProductCode[]>('/product-codes')

export async function fetchProduct(id: string): Promise<ProductFull> {
  const res = await fetch(`${resourceURL}/products/${id}`)
  if (res.ok) {
    return (await res.json()) as ProductFull
  }
  if (res.status === 404) {
    throw new Error('NOT_FOUND')
  }
  throw new Error('')
}

export function fetchCreateFullProduct(productUpdateFull: ProductSchema): Promise<Product> {
  const formItems: FormDataItem<File | any>[] = []
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

  return fetchChangeFormData('products', formItems, 'POST')
}

export function fetchUpdateFullProduct(
  productUpdateFull: ProductSchema,
  id: number | string,
): Promise<Product> {
  const formItems: FormDataItem<File | any>[] = []
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

  return fetchChangeFormData('products/' + id, formItems, 'PUT')
}

export async function fetchProductDeleteById(id: string) {
  return fetchDeleteSimple('products', id)
}

export async function fetchProductDeleteByIdIn(ids: string[]) {
  const query = toSearchParams('id', ids)
  const res = await fetch(`${resourceURL}/products?${query}`, { method: 'DELETE' })

  if (res.ok) {
    return
  }
  throw new Error('Fetch DELETE IN Failed')
}
