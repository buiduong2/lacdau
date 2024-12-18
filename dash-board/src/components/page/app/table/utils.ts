import {
  getCoreRowModel,
  getFilteredRowModel,
  getPaginationRowModel,
  getSortedRowModel,
  type ColumnDef,
  type FilterFn,
  type TableOptionsWithReactiveData,
} from '@tanstack/vue-table'
import type { TableFilterOption } from './types'

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
