<script setup lang="ts" generic="TData, TValue">
import type { TableAction } from '@/composables/useTableAction'
import { valueUpdater } from '@/lib/utils'
import type { Page } from '@/types'
import type { ColumnDef, ColumnFiltersState, VisibilityState } from '@tanstack/vue-table'
import { FlexRender, getCoreRowModel, useVueTable } from '@tanstack/vue-table'
import type { TableFacetedFilterInput, TableFacetedFilterPick } from './types'
import { getDefaultColumnVisibility, initialFitlerQuery } from './utils'
const props = defineProps<{
  columns: ColumnDef<TData, TValue>[]
  filterInputs: TableFacetedFilterInput[]
  filterPicks: TableFacetedFilterPick[]
  soringMapper?: Record<string, string>
  action: TableAction
  data: TData[]
  page: Page
}>()

const emit = defineEmits<{
  (e: 'queryChange'): void
}>()

const router = useRouter()
const route = useRoute()

const { sorting, sortQuery } = useSortQuery<any>(props.soringMapper || {}, route)

const { pagination, rowCount, pageCount, pageQuery } = usePageQuery(props, route)
const initialFilters: ColumnFiltersState = initialFitlerQuery(props.filterPicks, route)
const columnFilters = ref<ColumnFiltersState>(initialFilters)

const columnVisibility = ref<VisibilityState>(getDefaultColumnVisibility(props.columns))
const table = useVueTable({
  getCoreRowModel: getCoreRowModel(),
  manualSorting: true,
  manualPagination: true,
  manualFiltering: true,
  pageCount: pageCount.value,
  rowCount: rowCount.value,
  onSortingChange: (updaterOrValue) => valueUpdater(updaterOrValue, sorting),
  onColumnFiltersChange: (updaterOrValue) => valueUpdater(updaterOrValue, columnFilters),
  onColumnVisibilityChange: (updaterOrValue) => valueUpdater(updaterOrValue, columnVisibility),
  onPaginationChange: (updaterOrValue) => valueUpdater(updaterOrValue, pagination),
  get data() {
    return props.data
  },
  get columns() {
    return props.columns
  },
  state: {
    get sorting() {
      return sorting.value
    },
    get columnFilters() {
      return columnFilters.value
    },
    get columnVisibility() {
      return columnVisibility.value
    },
    get pagination() {
      return pagination.value
    },
  },
})

watch(
  [pageCount, rowCount],
  () => {
    table.setOptions((oldOptions) => {
      oldOptions.rowCount = 1

      return {
        ...oldOptions,
        get data() {
          return props.data
        },
        get columns() {
          return props.columns
        },
        pageCount: pageCount.value,
        rowCount: rowCount.value,
      }
    })
  },
  { immediate: false },
)

watch(
  [sorting, columnFilters, pagination],
  async () => {
    const filterQuery: Record<string, string[]> = {}
    columnFilters.value.forEach((col) => {
      filterQuery[col.id] = col.value as string[]
    })

    await router.push({
      query: {
        ...sortQuery.value,
        ...pageQuery.value,
        ...filterQuery,
      },
    })
    emit('queryChange')
  },
  {
    immediate: false,
  },
)
</script>

<template>
  <div class="space-y-4">
    <AppDataTableToolbar :action="action" :table="table" :filter-inputs="filterInputs" :filter-picks="filterPicks" />
    <div class="border rounded-md">
      <Table>
        <TableHeader>
          <TableRow v-for="headerGroup in table.getHeaderGroups()" :key="headerGroup.id">
            <TableHead>
              <Checkbox :checked="table.getIsAllRowsSelected()"
                @update:checked="(value) => table.toggleAllPageRowsSelected(!!value)" aria-label="Select all" />
            </TableHead>
            <TableHead v-for="header in headerGroup.headers" :key="header.id">
              <FlexRender v-if="!header.isPlaceholder" :render="header.column.columnDef.header"
                :props="header.getContext()" />
            </TableHead>
            <TableHead class="text-center">Action</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          <template v-if="table.getRowModel().rows?.length">
            <template v-for="row in table.getRowModel().rows" :key="row.id">
              <TableRow :data-state="row.getIsSelected() ? 'selected' : undefined">
                <TableCell>
                  <Checkbox :checked="row.getIsSelected()"
                    @update:checked="(value: boolean) => row.toggleSelected(!!value)" aria-label="Select Row" />
                </TableCell>
                <TableCell v-for="cell in row.getVisibleCells()" :key="cell.id">
                  <FlexRender :render="cell.column.columnDef.cell" :props="cell.getContext()" />
                </TableCell>
                <TableCell>
                  <div class="text-center">
                    <AppDataTableDropDown :row="row.original" :action="action" />
                  </div>
                </TableCell>
              </TableRow>
            </template>
          </template>
          <template v-else>
            <TableRow>
              <TableCell :colspan="columns.length + 2" class="h-24 text-center">
                No results.
              </TableCell>
            </TableRow>
          </template>
        </TableBody>
      </Table>
    </div>
    <AppDataTablePagination :table="table" />
  </div>
</template>
