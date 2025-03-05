<script setup lang="ts">
import AppDataTableClientPage from '@/components/page/app/table/AppDataTableClientPage.vue'
import type {
  TableFacetedFilterInput,
  TableFacetedFilterPick,
} from '@/components/page/app/table/types'
import {
  defaulthandleRemoveByIdIn,
  getTableClientDefaultConfig,
} from '@/components/page/app/table/utils'
import { columns } from '@/components/page/attribute-values/table/column'
import { toast } from '@/components/ui/toast'
import { useVueTable } from '@tanstack/vue-table'
import { storeToRefs } from 'pinia'

definePage({
  meta: { breadcrumb: 'Danh sách' },
})

const store = useAttributeStore()
await store.fetchInit()
const { attributeValues, attributes } = storeToRefs(store)
const table = useVueTable(getTableClientDefaultConfig(columns, attributeValues))

const filterPicks = computed<TableFacetedFilterPick[]>(() => [
  {
    columnId: 'attributeId',
    title: 'Nhóm thuộc tính',
    options: [
      { label: '--CHƯA CÓ--', value: -1 },
      ...attributes.value.map((a) => ({ label: a.name, value: a.id })),
    ],
  },
])

const filterInputs: TableFacetedFilterInput[] = [
  {
    name: 'id',
    placeholder: 'Tìm kiếm theo ID',
    label: 'ID',
  },
  {
    name: 'name',
    placeholder: 'Tìm kiếm theo tên',
    label: 'Tên',
  },
]

async function removeById(id: number | string) {
  id = Number(id)
  await fetchAttributeValueDelete(id)
  toast({ description: 'Xoá thành công' })
  store.removeAttributeValue(id)
}

const action = useTableAction({
  onCreate: '/attribute-values/create',
  onEdit: '/attribute-values/[id]',
  removeByIdIn(id, done) {
    defaulthandleRemoveByIdIn(id, done, toast)
  },
  onDelete: removeById,
})
</script>
<template>
  <AppDataTableClientPage
    :table="table"
    :action="action"
    :filter-inputs="filterInputs"
    :filter-picks="filterPicks"
    heading="Thuộc tính con"
    description="Danh sách thông tin tổng quan về các loại Thuộc tính con"
  />
</template>
