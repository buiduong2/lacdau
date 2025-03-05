<script setup lang="ts">
import { defaulthandleRemoveByIdIn } from '@/components/page/app/table/utils'
import { columns } from '@/components/page/orders/table/column'
import { getFilters } from '@/components/page/orders/table/filters'
import { toast } from '@/components/ui/toast'
import type { Page } from '@/types'
import type { OrderBasic } from '@/types/orders/resType'
definePage({
  meta: { breadcrumb: 'Danh Sách' },
})
const orders = ref<OrderBasic[]>([])
const page = ref<Page>()
async function fetchItem() {
  try {
    const {
      data: { content, page: pageRes },
    } = await fetchOrders()
    orders.value = content
    page.value = pageRes
  } catch (error) {
    console.log(error)
  }
}
await fetchItem()

const { filterInputs, filterPicks } = await getFilters()
const action = useTableAction({
  onCreate: '/orders/create',
  groupTop: [
    {
      icon: 'lucide:info',
      text: "Chi tiết",
      type: 'router',
      url: '/orders/[id]/'
    },
    {
      icon: 'lucide:git-fork',
      text: "Tiến trình",
      type: 'router',
      url: '/orders/[id]/process'
    }
  ],
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
        <h2 class="text-2xl font-bold tracking-tight">Đơn hàng</h2>
        <p class="text-muted-foreground">Danh sách thông tin tổng quan về đơn hàng</p>
      </div>
      <Button class="text-base w-[180px]" variant="default" @click="$router.push(action.createUrl)">
        <Icon icon="lucide:circle-plus" />Tạo mới
      </Button>
    </div>
    <AppDataTableServer :action="action" :filterInputs="filterInputs" :filter-picks="filterPicks" :data="orders"
      :columns="columns" :soring-mapper="{}" :page="page!" @query-change="onQueryChange" />
  </div>
</template>
