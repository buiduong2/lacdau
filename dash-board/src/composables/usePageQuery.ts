import type { Page } from '@/types'
import type { PaginationState } from '@tanstack/vue-table'
import type { RouteLocation } from 'vue-router'

interface Props {
  page: Page
}

export function usePageQuery(props: Props, route: RouteLocation) {
  const pagination = ref<PaginationState>({
    pageIndex: Number(route.query.page) || props.page.number,
    pageSize: Number(route.query.limit) || props.page.size,
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
