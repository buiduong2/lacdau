import type { Page } from '@/types'
import type { PaginationState } from '@tanstack/vue-table'
import type { RouteLocation } from 'vue-router'

interface Props {
  page: Page
}

export function usePageQuery<T>(props: Props, route: RouteLocation) {
  const pagination = ref<PaginationState>({
    pageIndex: Number(route.query.page) || 0,
    pageSize: Number(route.query.limit) || 10,
  })

  const pageQuery = computed(() => ({
    size: pagination.value.pageSize,
    page: pagination.value.pageIndex,
  }))

  return {
    pagination,
    rowCount: computed(() => props.page.totalElements),
    pageCount: computed(() => props.page.totalPages),
    pageQuery,
  }
}
