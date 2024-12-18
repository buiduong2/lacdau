<script setup lang="ts">
import AppDataTableClientPage from '@/components/page/app/table/AppDataTableClientPage.vue'
import type {
  TableFacetedFilterInput,
  TableFacetedFilterPick,
} from '@/components/page/app/table/types'
import { getTableClientDefaultConfig } from '@/components/page/app/table/utils'
import { columns } from '@/components/page/attribute-values/table/column'
import { toast } from '@/components/ui/toast'
import { useVueTable } from '@tanstack/vue-table'
import { storeToRefs } from 'pinia'

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

async function removeById(id: number) {
  await fetchAttributeValueDelete(id)
  toast({ description: 'Xoá thành công' })
  store.removeAttributeValue(id)
}
</script>
<template>
  <AppDataTableClientPage
    :table="table"
    :filter-inputs="filterInputs"
    :filter-picks="filterPicks"
    create-route-name="/attribute-values/create"
    update-route-name="/attribute-values/[id]"
    @remove-by-id="removeById"
    heading="Thuộc tính con"
    description="Danh sách thông tin tổng quan về các loại Thuộc tính con"
  />
</template>
