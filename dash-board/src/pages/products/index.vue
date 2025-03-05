<script setup lang="ts">
import { columns } from '@/components/page/product/table/column'
import { getFilters } from '@/components/page/product/table/filters'
import { toast } from '@/components/ui/toast'
import type { SortMapper } from '@/composables/useSortQuery'
import type { Page } from '@/types'
import type { Product } from '@/types/products/resTypes'
import { RouterLink } from 'vue-router'
definePage({
  meta: { breadcrumb: 'Danh sách' },
})
const sortMapper: SortMapper<Product> = {
  mainImage: 'mainImage.src',
}

const router = useRouter()
const products = ref<Product[]>([])

const { filterInputs, filterPicks } = await getFilters()
const page = ref<Page>()
async function fetchItem() {
  try {
    const { content, page: pageRes } = await fetchProducts()
    products.value = content
    page.value = pageRes
  } catch (error) {
    console.log(error)
  }
}

await fetchItem()

async function removeByIdIn(ids: string[] | number[], done: () => void) {
  fetchProductDeleteByIdIn(ids)
    .then(async () => {
      done()
      await fetchItem()
      toast({ description: `Xóa Product ${JSON.stringify(ids)} Thành công` })
    })
    .catch(() =>
      toast({ description: `Xóa Product ${JSON.stringify(ids)} thất bại`, variant: 'destructive' }),
    )
}

async function removeById(id: string | number) {
  fetchProductDeleteById(id)
    .then(async () => {
      await fetchItem()
      toast({ description: `Xóa Product ${id} thành công` })
    })
    .catch(() => toast({ description: `Xóa Product ${id} thất bại`, variant: 'destructive' }))
}

function cloneById(id: string | number) {
  toast({ description: `Đang chuyển trang` })
  router.push({ name: '/products/create', query: { id } })
}

function onQueryChange() {
  fetchItem()
}

const action = useTableAction({
  onCreate: '/products/create',
  removeByIdIn: removeByIdIn,
  onDelete: removeById,
  onEdit: '/products/[id]',
  groupTop: [
    {
      type: 'custom',
      click: cloneById,
      icon: 'lucide:copy',
      text: 'Tạo bản sao',
    },
  ],
})
</script>

<template>
  <div class="h-full flex-1 flex-col space-y-8 p-8 md:flex">
    <div class="flex items-center justify-between space-y-2">
      <div>
        <h2 class="text-2xl font-bold tracking-tight">Sản phẩm!</h2>
        <p class="text-muted-foreground">Danh sách thông tin tổng quan về sản phẩm</p>
      </div>
      <Button class="text-base w-[180px]" variant="default" @click="router.push(action.createUrl)">
        <Icon icon="lucide:circle-plus" />Tạo mới</Button
      >
    </div>
    <AppDataTableServer
      :action="action"
      :filterInputs="filterInputs"
      :filter-picks="filterPicks"
      :data="products"
      :columns="columns"
      :soring-mapper="sortMapper"
      :page="page!"
      @query-change="onQueryChange"
    />
  </div>
</template>
