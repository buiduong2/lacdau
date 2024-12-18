<script setup lang="ts">
import { getSchema } from '@/components/page/brands/form/schema'
import { toast } from '@/components/ui/toast'
import type { BrandUpdate } from '@/types/brands/reqTypes'

const router = useRouter()
const route = useRoute('/brands/[id]')
const brand = await fetchBrand(Number(route.params.id))
const store = useBrandStore()
const schema = getSchema(brand)

async function onSubmit(formValues: BrandUpdate) {
  const res = await fetchBrandEdit(formValues, brand.id)
  toast({ description: 'Thêm Brand thành công' })
  store.setDirty()
  router.push('/brands')
}
</script>
<template>
  <AppFormLayout title="Tạo Brand mới" back-to="/brands/">
    <AppFormGroup title="Tông quan" desc="Nhập thông tin tổng quan về Brand">
      <AutoForm :schema="schema" @submit="onSubmit">
        <Button type="submit" class="mt-5"> Submit </Button>
      </AutoForm>
    </AppFormGroup>
  </AppFormLayout>
</template>

<style scoped></style>
