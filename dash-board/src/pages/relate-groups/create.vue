<script setup lang="ts">
import { schema } from '@/components/page/relate-groups/form/schema'
import { toast } from '@/components/ui/toast'
import { toTypedSchema } from '@vee-validate/zod'
import { useForm } from 'vee-validate'
definePage({
  meta: { breadcrumb: 'Tạo mới' },
})
const form = useForm({
  validationSchema: toTypedSchema(schema),
})

const store = useRelateStore()

const onSubmit = form.handleSubmit(async (formValue) => {
  await fetchRelateGroupCreate(formValue)
  store.setDirty()
  form.resetForm()
  toast({ description: 'Thêm nhóm thành công' })
})
</script>
<template>
  <RelateGroupForm @submit="onSubmit" />
</template>

<style scoped></style>
