<script setup lang="ts">
import attributeValues from '@/data/attribute-values.json'
import brands from '@/data/brands.json'
import categories from '@/data/categories.json'
import type { Table } from '@tanstack/vue-table'
import type { TableFacetedFilterPick, TableFilterOption } from '../../app/table/types'
import { ProductStatus } from '@/utils/types'
const props = defineProps<{ table: Table<any> }>()
defineEmits<{
  (e: 'removeSelectedRow'): void
}>()

const isFiltered = computed(() => props.table.getState().columnFilters.length > 0)

const attrValFilters: TableFilterOption[] = toTableoption(attributeValues)
const brandFilters: TableFilterOption[] = toTableoption(brands)
const categoryFilters: TableFilterOption[] = toTableoption(categories)

const keyword = ref<string>('')

function submitSearch() {
  props.table.getColumn('search')?.setFilterValue(keyword)
}

function resetFilter() {
  props.table.resetColumnFilters()
  keyword.value = ''
}

const facetedFilters: TableFacetedFilterPick[] = [
  { columnId: 'categoryIds', options: categoryFilters, title: 'Category' },
  { columnId: 'brandIds', options: brandFilters, title: 'Brand' },
  { columnId: 'attributeValueIds', options: attrValFilters, title: 'Attribute Value' },
  {
    columnId: 'status',
    options: Object.values(ProductStatus).map((status) => ({ label: status, value: status })),
    title: 'Status',
  },
]
</script>

<template>
  <div class="flex items-center justify-between">
    <Button
      v-if="table.getIsSomeRowsSelected() || table.getIsAllRowsSelected()"
      variant="destructive"
      @click="$emit('removeSelectedRow')"
      >Xóa</Button
    >
    <div class="flex flex-1 items-center space-x-2">
      <form @submit.prevent="submitSearch">
        <Input
          placeholder="Tìm kiếm sản phẩm theo tên, Id"
          class="h-8 w-[150px] lg:w-[250px]"
          v-model="keyword"
        />
      </form>

      <DataTableFacetedFilter
        :options="filter.options"
        :column="table.getColumn(filter.columnId)!"
        :title="filter.title"
        v-for="filter in facetedFilters"
      />

      <Button v-if="isFiltered" variant="ghost" class="h-8 px-2 lg:px-3" @click="resetFilter">
        Reset
      </Button>
    </div>
    <DataTableViewOptions :table="table" />
  </div>
</template>
