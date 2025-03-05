import {
  getCoreRowModel,
  getFilteredRowModel,
  getPaginationRowModel,
  getSortedRowModel,
  type ColumnDef,
  type ColumnFiltersState,
  type FilterFn,
  type TableOptionsWithReactiveData,
} from '@tanstack/vue-table'
import type { TableFacetedFilterPick, TableFilterOption } from './types'

interface Entity {
  id: number
  name: string
}

export function toTableoption(entities: Entity[]): TableFilterOption[] {
  return entities.map((e) => ({
    label: e.name,
    value: e.id,
  }))
}

export function getTableClientDefaultConfig<T>(
  columns: ColumnDef<T, any>[],
  data: globalThis.Ref<T[]>,
): TableOptionsWithReactiveData<T> {
  return {
    enableHiding: true,
    enableSorting: true,
    autoResetPageIndex: true,
    getPaginationRowModel: getPaginationRowModel(),
    getCoreRowModel: getCoreRowModel(),
    getSortedRowModel: getSortedRowModel(),
    getFilteredRowModel: getFilteredRowModel(),
    columns: columns,
    data: data,
  }
}

export const filterPickFn: FilterFn<any> = (row, id, value) => {
  if (value === undefined) {
    return true
  }

  if (Array.isArray(value)) {
    value = value.map((value) => (value === -1 ? null : value))
    return value.includes(row.getValue(id))
  }

  throw new Error('Value must be an array')
}

export function initialFitlerQuery(
  filterPicks: TableFacetedFilterPick[],
  route: ReturnType<typeof useRoute>,
): ColumnFiltersState {
  return filterPicks
    .map((f) => {
      const id = f.columnId
      const query = route.query[id]
      if (!query) {
        return { id, value: undefined }
      }
      return {
        id,
        value: Array.isArray(query) ? query : [query],
      }
    })
    .filter((f) => f.value)
}

export function getDefaultColumnVisibility(
  columns: ColumnDef<any, any>[],
): Record<string, boolean> {
  return Object.fromEntries(
    columns
      .filter((col) => Object.keys(col).length === 1)
      .map((col) => [[col.id as string], false]),
  )
}

export function defaulthandleRemoveByIdIn(ids: number[] | string[], done: () => void, toast: any) {
  toast({ description: 'Hiện tại chưa có chức năng xóa ' + JSON.stringify(ids) })
  done()
}
