<script setup lang="ts">
import {
  defaulthandleRemoveByIdIn,
  getTableClientDefaultConfig,
} from '@/components/page/app/table/utils'
import { columns } from '@/components/page/roles/table/column'
import { getFilters } from '@/components/page/roles/table/filters'
import { toast } from '@/components/ui/toast'
import { useVueTable } from '@tanstack/vue-table'
import { storeToRefs } from 'pinia'

definePage({
  meta: { breadcrumb: 'Danh Sách' },
})

const store = useRoleStore()
await store.fetchInit()
const { userPermissions } = storeToRefs(store)
const table = useVueTable(getTableClientDefaultConfig(columns, userPermissions))
const { filterInputs, filterPicks } = await getFilters()
const action = useTableAction({
  onCreate: '/roles/create',
  removeByIdIn: (ids, done) => {
    defaulthandleRemoveByIdIn(ids, done, toast)
  },
  onEdit: '/roles/[id]',
})
</script>
<template>
  <AppDataTableClientPage
    heading="Vai trò"
    :table="table"
    :action="action"
    :filter-inputs="filterInputs"
    :filter-picks="filterPicks"
    description="Danh sách thông tin tổng quan về các Quyền của người dùng trong hệ thống"
  />
</template>
