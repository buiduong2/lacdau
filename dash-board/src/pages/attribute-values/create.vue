<script setup lang="ts">
import { schema } from '@/components/page/attribute-values/form/schema'
import { toast } from '@/components/ui/toast'
import { toTypedSchema } from '@vee-validate/zod'
import { useForm } from 'vee-validate'

definePage({
  meta: { breadcrumb: 'Tạo mới' },
})

const form = useForm({
  validationSchema: toTypedSchema(schema),
})

const store = useAttributeStore()

const submit = form.handleSubmit(async (formValue) => {
  await fetchAttributeValueCreate(formValue)

  toast({ description: 'Tạo AttributeValuse thành công' })
  store.setDirty()
  form.resetForm({})
})
</script>
<template>
  <AttributeValueForm pageTitle="Tạo mới thuộc tính con" @submit="submit" :form="form" />
</template>
