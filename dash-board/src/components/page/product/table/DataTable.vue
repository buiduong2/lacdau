<script setup lang="ts" generic="TData, TValue">
import type {
  ColumnDef,
  ColumnFiltersState,
  ExpandedState,
  VisibilityState,
} from '@tanstack/vue-table'

import { valueUpdater } from '@/lib/utils'
import { FlexRender, getCoreRowModel, useVueTable } from '@tanstack/vue-table'
import type {
  TableFacetedFilterInput,
  TableFacetedFilterPick,
  TableFilterOption,
} from '../../app/table/types'
import type { Page } from '@/types'
import type { Product } from '@/types/products/resTypes'
const props = defineProps<{
  columns: ColumnDef<TData, TValue>[]
  soringMapper?: Record<string, string>
  data: TData[]
  page: Page
}>()

const emit = defineEmits<{
  (e: 'removeByIdIn', action: { payload: string[]; done: () => void }): void
  (e: 'removeById', payload: string): void
  (e: 'editById', payload: string): void
  (e: 'cloneById', payload: string): void
  (e: 'queryChange'): void
}>()

const router = useRouter()
const route = useRoute()

const { sorting, sortQuery } = useSortQuery<Product>(props.soringMapper || {}, route)

const { pagination, rowCount, pageCount, pageQuery } = usePageQuery(props, route)

const initialFilters = [
  { id: 'categoryIds', value: route.query.categoryIds },
  { id: 'brandIds', value: route.query.brandIds },
  { id: 'attributeValueIds', value: route.query.attributeValueIds },
  {
    id: 'status',
    value: Array.isArray(route.query.status)
      ? route.query.status
      : route.query.status && [route.query.status],
  },
].filter((f) => f.value)
const columnFilters = ref<ColumnFiltersState>(initialFilters)

const columnVisibility = ref<VisibilityState>({
  brandIds: false,
  categoryIds: false,
  attributeValueIds: false,
  search: false,
})
const expanded = ref<ExpandedState>({})
const rowSelection = ref({})
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
  onRowSelectionChange: (updaterOrValue) => valueUpdater(updaterOrValue, rowSelection),
  onExpandedChange: (updaterOrValue) => valueUpdater(updaterOrValue, expanded),
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
    get rowSelection() {
      return rowSelection.value
    },
    get expanded() {
      return expanded.value
    },
    get pagination() {
      return pagination.value
    },
  },
})

function onRemoveSelectedRow() {
  emit('removeByIdIn', {
    payload: table.getSelectedRowModel().rows.map((row) => row.getValue('id') as string),
    done: () => table.toggleAllRowsSelected(false),
  })
}

watch([pageCount, rowCount], () => {
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
})

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

interface Entity {
  id: number
  name: string
}

function toTableoption(entities: Entity[]): TableFilterOption[] {
  return entities.map((e) => ({
    label: e.name,
    value: String(e.id),
  }))
}
const attributeStore = useAttributeStore()
await attributeStore.fetchInit()
const brandStore = useBrandStore()
await brandStore.fetchInit()
const categoryStore = useCategoryStore()
await categoryStore.fetchInit()

const attrValFilters: TableFilterOption[] = toTableoption(attributeStore.attributeValues)
const brandFilters: TableFilterOption[] = toTableoption(brandStore.brands)
const categoryFilters: TableFilterOption[] = toTableoption(categoryStore.categoies)

const fileterInput: TableFacetedFilterInput[] = [
  {
    label: 'Tên',
    name: 'search',
    placeholder: 'Sản phẩm A',
  },
]

const filterPicks: TableFacetedFilterPick[] = [
  { columnId: 'categoryIds', options: categoryFilters, title: 'Category' },
  { columnId: 'brandIds', options: brandFilters, title: 'Brand' },
  { columnId: 'attributeValueIds', options: attrValFilters, title: 'Attribute Value' },
  {
    columnId: 'status',
    options: [
      { label: 'ACTIVE', value: 'ACTIVE' },
      { label: 'ARCHIVED', value: 'ARCHIVED' },
      { label: 'DRAFT', value: 'DRAFT' },
    ],
    title: 'status',
  },
]
</script>

<template>
  <div class="space-y-4">
    <AppDataTableToolbar
      :table="table"
      @remove-selected-row="onRemoveSelectedRow"
      :filter-inputs="fileterInput"
      :filter-picks="filterPicks"
    />
    <div class="border rounded-md">
      <Table>
        <TableHeader>
          <TableRow v-for="headerGroup in table.getHeaderGroups()" :key="headerGroup.id">
            <TableHead>
              <Checkbox
                :checked="table.getIsAllRowsSelected()"
                @update:checked="(value) => table.toggleAllPageRowsSelected(!!value)"
                aria-label="Select all"
              />
            </TableHead>
            <TableHead v-for="header in headerGroup.headers" :key="header.id">
              <FlexRender
                v-if="!header.isPlaceholder"
                :render="header.column.columnDef.header"
                :props="header.getContext()"
              />
            </TableHead>
            <TableHead class="text-center">Action</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          <template v-if="table.getRowModel().rows?.length">
            <template v-for="row in table.getRowModel().rows" :key="row.id">
              <TableRow :data-state="row.getIsSelected() ? 'selected' : undefined">
                <TableCell>
                  <Checkbox
                    :checked="row.getIsSelected()"
                    @update:checked="(value: boolean) => row.toggleSelected(!!value)"
                    aria-label="Select Row"
                  />
                </TableCell>
                <TableCell v-for="cell in row.getVisibleCells()" :key="cell.id">
                  <FlexRender :render="cell.column.columnDef.cell" :props="cell.getContext()" />
                </TableCell>
                <TableCell>
                  <div class="text-center">
                    <AppDataTableDropDown
                      :row="row.original"
                      @edit="(payload) => $emit('editById', payload)"
                      @clone="(payload) => $emit('cloneById', payload)"
                      @remove="(payload) => $emit('removeById', payload)"
                    />
                  </div>
                </TableCell>
              </TableRow>
            </template>
          </template>
          <template v-else>
            <TableRow>
              <TableCell :colspan="columns.length" class="h-24 text-center">
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
