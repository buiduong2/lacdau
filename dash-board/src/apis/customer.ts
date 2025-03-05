import type { PageResponse } from '@/types'
import type { CustomerBasic } from '@/types/customers/resType'

export async function fetchCustomers() {
  return getResourceClient().get<PageResponse<CustomerBasic>>(
    '/api/admin/customers' + window.location.search.toString(),
  )
}

export async function fetchCustomer(id: number | string) {}
