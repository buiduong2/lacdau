import type { ColumnSort } from '@tanstack/vue-table'
import type { LocationQueryValue, RouteLocation } from 'vue-router'

export type SortMapper<T> = Partial<Record<keyof T, string>>

export function useSortQuery<E extends Record<string, any>>(
  mapper: SortMapper<E>,
  route: RouteLocation,
) {
  const mapKey = mapper
  const mapValue = Object.fromEntries(Object.entries(mapper).map(([key, value]) => [value, key]))

  function mappingToQuery(sort: ColumnSort): string {
    return `${mapKey[sort.id] || sort.id},${sort.desc ? 'desc' : 'asc'}`
  }

  function mappingtoSortColumn(field: string, direction: string): ColumnSort {
    return {
      id: `${mapValue[field] ? mapValue[field] : field}`,
      desc: direction === 'desc',
    }
  }

  const sorting = ref<ColumnSort[]>(initialState())
  const sortQuery = computed(() => ({
    sort: sorting.value.map(mappingToQuery),
  }))

  function initialState(): ColumnSort[] {
    const sort: LocationQueryValue | LocationQueryValue[] = route.query.sort
    if (Array.isArray(sort)) {
      return sort
        .filter((s) => String(s).includes(','))
        .map((s) => String(s).split(','))
        .filter((s) => s.length === 2)
        .map((s) => mappingtoSortColumn(s[0], s[1]))
    }

    const arr = String(sort).split(',')
    if (arr.length == 2) {
      return [mappingtoSortColumn(arr[0], arr[1])]
    }

    return []
  }

  return {
    sorting,
    sortQuery,
  }
}
