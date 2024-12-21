<script setup lang="ts">
import AppDataTableClientPage from '@/components/page/app/table/AppDataTableClientPage.vue'
import type { TableFacetedFilterInput } from '@/components/page/app/table/types'
import { getTableClientDefaultConfig } from '@/components/page/app/table/utils'
import { columns } from '@/components/page/relate-groups/table/column'
import { useVueTable } from '@tanstack/vue-table'
import { storeToRefs } from 'pinia'
definePage({
  meta: { breadcrumb: ['Nhóm sản phẩm', 'Danh sách'] },
})
const store = useRelateStore()
await store.fetchInit()
const { relateGroups } = storeToRefs(store)
const table = useVueTable(getTableClientDefaultConfig(columns, relateGroups))

const filterInputs: TableFacetedFilterInput[] = [
  {
    name: 'name',
    placeholder: 'Tìm kiếm theo tên',
    label: 'Tên',
  },
]
</script>
<template>
  <AppDataTableClientPage
    create-route-name="/relate-groups/create"
    update-route-name="/relate-groups/[id]"
    :table="table"
    :filter-inputs="filterInputs"
    heading="Nhóm sản phẩm"
    description="Danh sách thông tin tổng quan về các loại nhóm sản phẩm"
  />
</template>
