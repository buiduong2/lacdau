<script setup lang="ts">
import { toTypedSchema } from '@vee-validate/zod'
import { useForm } from 'vee-validate'
import { toast } from '@/components/ui/toast'
import { zodSchema } from '@/components/page/product/form/schema'

const router = useRouter()
const route = useRoute()
const cloneId = route.query.id
let prototype = {}

if (cloneId) {
  try {
    prototype = await fetchProduct(String(cloneId))
  } catch (error) {
    toast({ variant: 'destructive', description: 'Có lỗi xảy ra khi Clone' })
  }
}

const formSchema = toTypedSchema(zodSchema)

const form = useForm({
  validationSchema: formSchema,
  initialValues: prototype,
})

const onSubmit = form.handleSubmit(async (values) => {
  const res = await fetchCreateFullProduct(values)

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
    <ProductForm :form="form" :onSubmit="onSubmit" />
  </div>
</template>
