<script setup lang="ts">
import AppDataTableClientPage from '@/components/page/app/table/AppDataTableClientPage.vue'
import type { TableFacetedFilterInput } from '@/components/page/app/table/types'
import {
  defaulthandleRemoveByIdIn,
  getTableClientDefaultConfig,
} from '@/components/page/app/table/utils'
import { columns } from '@/components/page/relate-groups/table/column'
import { toast } from '@/components/ui/toast'
import { useVueTable } from '@tanstack/vue-table'
import { storeToRefs } from 'pinia'
definePage({
  meta: { breadcrumb: 'Danh sách' },
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

const action = useTableAction({
  onCreate: '/relate-groups/create',
  onEdit: '/relate-groups/[id]',
  removeByIdIn: (id, done) => {
    defaulthandleRemoveByIdIn(id, done, toast)
  },
})
</script>
<template>
  <AppDataTableClientPage
    :action="action"
    :table="table"
    :filter-inputs="filterInputs"
    heading="Nhóm sản phẩm"
    description="Danh sách thông tin tổng quan về các loại nhóm sản phẩm"
  />
</template>
