<script setup lang="ts">
import AppDataTableClientPage from '@/components/page/app/table/AppDataTableClientPage.vue'
import type {
  TableFacetedFilterInput,
  TableFacetedFilterPick,
} from '@/components/page/app/table/types'
import { getTableClientDefaultConfig } from '@/components/page/app/table/utils'
import { columns } from '@/components/page/brands/table/column'
import { toast } from '@/components/ui/toast'
import { useVueTable } from '@tanstack/vue-table'
import { storeToRefs } from 'pinia'
definePage({
  meta: { breadcrumb: ['Thương hiệu', 'Danh sách'] },
})
const store = useBrandStore()
await store.fetchInit()
const { brands } = storeToRefs(store)
const table = useVueTable(getTableClientDefaultConfig(columns, brands))

const filterPicks = computed<TableFacetedFilterPick[]>(() => [])

const filterInputs: TableFacetedFilterInput[] = [
  {
    name: 'name',
    placeholder: 'Tìm kiếm theo tên',
    label: 'Tên',
  },
]

async function deleteById(id: number) {
  await fetchBrandDelete(id)
  store.remove(id)

  toast({ description: 'Xóa Brand thành công' })
}
</script>
<template>
  <AppDataTableClientPage
    :table="table"
    :filter-inputs="filterInputs"
    :filter-picks="filterPicks"
    @remove-by-id="deleteById"
    create-route-name="/brands/create"
    update-route-name="/brands/[id]"
    heading="Thương hiệu!"
    description="Danh sách thông tin tổng quan về các loại thương hiệu"
  />
</template>
