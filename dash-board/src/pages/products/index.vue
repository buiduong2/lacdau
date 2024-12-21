<script setup lang="ts">
import { columns } from '@/components/page/product/table/column'
import { toast } from '@/components/ui/toast'
import type { SortMapper } from '@/composables/useSortQuery'
import type { Page } from '@/types'
import type { Product } from '@/types/products/resTypes'
import { RouterLink } from 'vue-router'
definePage({
  meta: { breadcrumb: ['Sản phẩm', 'Danh sách'] },
})
const sortMapper: SortMapper<Product> = {
  mainImage: 'mainImage.src',
}

const router = useRouter()
const products = ref<Product[]>([])
const page = ref<Page>()
async function fetchItem() {
  const { content, page: pageRes } = await fetchProducts()
  products.value = content
  page.value = pageRes
}

await fetchItem()

async function removeByIdIn({ payload: ids, done }: { payload: string[]; done: () => void }) {
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

async function removeById(id: string) {
  fetchProductDeleteById(id)
    .then(async () => {
      await fetchItem()
      toast({ description: `Xóa Product ${id} thành công` })
    })
    .catch(() => toast({ description: `Xóa Product ${id} thất bại`, variant: 'destructive' }))
}

function editById(id: string) {
  toast({ description: `Đang chuyển trang` })
  router.push({ name: '/products/[id]', params: { id } })
}

function cloneById(id: string) {
  toast({ description: `Đang chuyển trang` })
  router.push({ name: '/products/create', query: { id } })
}

function onQueryChange() {
  fetchItem()
}
</script>

<template>
  <div class="h-full flex-1 flex-col space-y-8 p-8 md:flex">
    <div class="flex items-center justify-between space-y-2">
      <div>
        <h2 class="text-2xl font-bold tracking-tight">Sản phẩm!</h2>
        <p class="text-muted-foreground">Danh sách thông tin tổng quan về sản phẩm</p>
      </div>
      <Button class="text-base w-[180px]" variant="default" :as="RouterLink" to="/products/create">
        <Icon icon="lucide:circle-plus" />Tạo mới</Button
      >
    </div>
    <DataTable
      :data="products"
      :columns="columns"
      :soring-mapper="sortMapper"
      :page="page!"
      @query-change="onQueryChange"
      @remove-by-id="removeById"
      @edit-by-id="editById"
      @clone-by-id="cloneById"
      @remove-by-id-in="removeByIdIn"
    />
  </div>
</template>
