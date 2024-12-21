<script setup lang="ts">
import { toTypedSchema } from '@vee-validate/zod'
import { useForm } from 'vee-validate'
import { toast } from '@/components/ui/toast'
import { zodSchema } from '@/components/page/product/form/schema'
import type { ProductFull } from '@/types/products/resTypes'

definePage({
  meta: { breadcrumb: ['Danh mục', 'Chỉnh sửa'] },
})

const router = useRouter()
const route = useRoute('/products/[id]')
const cloneId = route.params.id
let prototype: ProductFull | undefined = undefined

if (cloneId) {
  try {
    prototype = await fetchProduct(String(cloneId))
  } catch (error) {
    toast({ variant: 'destructive', description: 'Có lỗi xảy ra khi Clone' })
  }
}
if (!prototype) {
  throw new Error('not FOUnd')
}

const formSchema = toTypedSchema(zodSchema)

const form = useForm({
  validationSchema: formSchema,
  initialValues: {
    ...prototype,
    mainImage: undefined,
    thumbnails: undefined,
  },
})

const onSubmit = form.handleSubmit(async (values) => {
  const res = await fetchUpdateFullProduct(values, cloneId)
  toast({
    description: 'Thêm product thành công',
  })
  router.push({
    name: '/products/[id]',
    params: { id: res.id },
  })
})
</script>

<template>
  <div class="h-full flex-1 flex-col space-y-8 p-8 md:flex">
    <ProductForm :form="form" :onSubmit="onSubmit" :product="prototype" />
  </div>
</template>
