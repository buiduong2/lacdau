<script setup lang="ts">
import AppDataTableClientPage from '@/components/page/app/table/AppDataTableClientPage.vue'
import type { TableFacetedFilterInput } from '@/components/page/app/table/types'
import { getTableClientDefaultConfig } from '@/components/page/app/table/utils'
import { columns } from '@/components/page/attributes/table/column'
import { toast } from '@/components/ui/toast'
import { useVueTable } from '@tanstack/vue-table'
import { storeToRefs } from 'pinia'

definePage({
  meta: { breadcrumb: ['Thuộc tính', 'Danh sách'] },
})
const store = useAttributeStore()
await store.fetchInit()
const { attributes } = storeToRefs(store)
const table = useVueTable(getTableClientDefaultConfig(columns, attributes))

const filterInputs: TableFacetedFilterInput[] = [
  {
    name: 'name',
    placeholder: 'Tìm kiếm theo tên',
    label: 'Tên',
  },
]

async function deleteById(id: number) {
  await fetchAttributeDelete(id)
  store.removeAttribute(id)
  store.setDirty()
  toast({ description: 'Xóa thành công' })
}
</script>
<template>
  <AppDataTableClientPage
    :table="table"
    :filter-inputs="filterInputs"
    create-route-name="/attributes/create"
    update-route-name="/attributes/[id]"
    @remove-by-id="deleteById"
    heading="Nhóm thuộc tính!"
    description="Danh sách thông tin tổng quan về các loại nhóm thuộc tính"
  />
</template>
