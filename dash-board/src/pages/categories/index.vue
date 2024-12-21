<script setup lang="ts">
import { fetchCategoryDelete } from '@/apis/categories'
import AppDataTableClientPage from '@/components/page/app/table/AppDataTableClientPage.vue'
import type {
  TableFacetedFilterInput,
  TableFacetedFilterPick,
} from '@/components/page/app/table/types'
import { getTableClientDefaultConfig } from '@/components/page/app/table/utils'
import { columns } from '@/components/page/categories/table/column'
import { toast } from '@/components/ui/toast'
import { useVueTable } from '@tanstack/vue-table'
import { storeToRefs } from 'pinia'
definePage({
  meta: { breadcrumb: ['Danh mục', 'Danh sách'] },
})
const store = useCategoryStore()
await store.fetchInit()
const { categoies } = storeToRefs(store)
const table = useVueTable(getTableClientDefaultConfig(columns, categoies))

const filterPicks = computed<TableFacetedFilterPick[]>(() => [
  {
    columnId: 'level',
    title: 'Độ sâu',
    options: [...new Set(categoies.value.map((cat) => cat.level))].map((level) => ({
      label: `level: ${level}`,
      value: level,
    })),
  },
])

const filterInputs: TableFacetedFilterInput[] = [
  {
    name: 'name',
    placeholder: 'Tìm kiếm theo tên',
    label: 'Tên',
  },
  {
    name: 'parentId',
    placeholder: 'Tìm kiếm theo id của Cha',
    label: 'Độ sâu',
  },
]

async function deleteById(id: number) {
  try {
    await fetchCategoryDelete(id)
    toast({ description: 'Xóa category thành congo' })
    store.setDirty()
    await store.fetchInit()
  } catch (error) {
    toast({ description: 'Xóa Category thất bại', type: 'foreground' })
  }
}
</script>
<template>
  <AppDataTableClientPage
    :table="table"
    :filter-inputs="filterInputs"
    :filter-picks="filterPicks"
    create-route-name="/categories/create"
    update-route-name="/categories/[id]"
    @remove-by-id="deleteById"
    heading="Loại mặt hàng!"
    description="Danh sách thông tin tổng quan về các loại mặt hàng"
  />
</template>
