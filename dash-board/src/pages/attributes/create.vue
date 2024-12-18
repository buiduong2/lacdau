<script setup lang="ts">
import { schema } from '@/components/page/attributes/form/createSchema'
import { toast } from '@/components/ui/toast'
import { toTypedSchema } from '@vee-validate/zod'
import { useForm } from 'vee-validate'

const store = useAttributeStore()
const form = useForm({
  validationSchema: toTypedSchema(schema),
})

const onSubmit = form.handleSubmit(async (formValue) => {
  await fetchAttributeCreate(formValue)
  store.setDirty()
  toast({ description: 'Thêm Attribute thành công' })
  form.resetForm()
})
</script>
<template>
  <AppFormLayout title="Tạo Attribute Mới">
    <form class="flex gap-4 flex-col" @submit="onSubmit">
      <AttributeFormInfo />

      <AttributeFormAddAttributeValue />

      <Button>Xác nhận</Button>
    </form>
  </AppFormLayout>
</template>
