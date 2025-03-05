<script setup lang="ts">
import { defaulthandleRemoveByIdIn } from '@/components/page/app/table/utils'
import { columns } from '@/components/page/customers/table/column'
import { getFilters } from '@/components/page/customers/table/filters'
import { toast } from '@/components/ui/toast'
import type { Page } from '@/types'
import type { CustomerBasic } from '@/types/customers/resType'
definePage({
  meta: { breadcrumb: 'Danh Sách' },
})
const customers = ref<CustomerBasic[]>([])
const page = ref<Page>()
async function fetchItem() {
  try {
    const {
      data: { content, page: pageRes },
    } = await fetchCustomers()
    customers.value = content
    page.value = pageRes
  } catch (error) {
    console.log(error)
  }
}
await fetchItem()

const { filterInputs } = await getFilters()
const action = useTableAction({
  onCreate: '/customers/create',
  onEdit: '/customers/[id]',
  removeByIdIn: (ids, done) => {
    defaulthandleRemoveByIdIn(ids, done, toast)
  },
})
async function onQueryChange() {
  fetchItem()
}
</script>
<template>
  <div class="h-full flex-1 flex-col space-y-8 p-8 md:flex">
    <div class="flex items-center justify-between space-y-2">
      <div>
        <h2 class="text-2xl font-bold tracking-tight">Khách hàng</h2>
        <p class="text-muted-foreground">Danh sách thông tin tổng quan về khách hàng</p>
      </div>
      <Button class="text-base w-[180px]" variant="default" @click="$router.push(action.createUrl)">
        <Icon icon="lucide:circle-plus" />Tạo mới
      </Button>
    </div>
    <AppDataTableServer :action="action" :filterInputs="filterInputs" :filter-picks="[]" :data="customers"
      :columns="columns" :soring-mapper="{}" :page="page!" @query-change="onQueryChange" />
  </div>
</template>
