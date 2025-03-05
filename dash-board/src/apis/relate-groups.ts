import type { ProductRelateInfo, RelateGroupCreate } from '@/types/relate-groups/reqTypes'
import type { RelateGroup, RelateGroupBasic, RelateGroupInfo } from '@/types/relate-groups/resTypes'

export const fetchRelateGroups = () => fetchResouceSimple<RelateGroup[]>('/api/admin/relate-groups')

export const fetchRelateGroupInfo = (id: number) =>
  fetchResouceSimple<RelateGroupInfo>('/api/admin/relate-groups/' + id + '/relate-infos')

export async function fetchRelateGroup(id: number): Promise<RelateGroupBasic> {
  return fetchResouceSimple('/api/admin/relate-groups/' + id)
}

export async function fetchRelateGroupCreate(data: RelateGroupCreate) {
  return fetchChangeSimple<RelateGroupCreate, RelateGroupBasic>(
    '/api/admin/relate-groups',
    data,
    'POST',
  )
}

export async function fetchRelateGroupUpdate(data: RelateGroupCreate, id: number) {
  return fetchChangeSimple<RelateGroupCreate, RelateGroupBasic>(
    '/api/admin/relate-groups/' + id,
    data,
    'PUT',
  )
}

export async function fetchProductRelate(id: string): Promise<ProductRelateInfo> {
  return fetchResouceSimple('/api/admin/products/' + id + '/relate-info')
}

export async function fetchProductRelateByGroupId(id: number): Promise<ProductRelateInfo[]> {
  return fetchResouceSimple('/api/admin/products/relate-info/relate-group/' + id)
}
