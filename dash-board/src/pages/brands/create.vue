<script setup lang="ts">
import { getSchema } from '@/components/page/brands/form/schema'
import { toast } from '@/components/ui/toast'
import type { BrandUpdate } from '@/types/brands/reqTypes'
definePage({
  meta: { breadcrumb: ['Thương hiệu', 'Tạo mới'] },
})
const router = useRouter()
const store = useBrandStore()
const schema = getSchema()

async function onSubmit(formValues: BrandUpdate) {
  const res = await fetchBrandCreate(formValues)
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
