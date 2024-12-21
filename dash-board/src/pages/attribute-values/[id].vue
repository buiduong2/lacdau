<script setup lang="ts">
import { schema } from '@/components/page/attribute-values/form/schema'
import { toast } from '@/components/ui/toast'
import { toTypedSchema } from '@vee-validate/zod'
import { useForm } from 'vee-validate'

definePage({
  meta: { breadcrumb: ['Thuộc tính con', 'Chỉnh sửa'] },
})

const route = useRoute('/attribute-values/[id]')
const id = route.params.id

const oldAttrValue = await fetchAttributeValue(Number(id))

const form = useForm({
  validationSchema: toTypedSchema(schema),
  initialValues: oldAttrValue,
})

const store = useAttributeStore()

const submit = form.handleSubmit(async (formValue) => {
  await fetchAttributeValueEdit(formValue, Number(id))

  toast({ description: 'Chỉnh sửa AttributeValue thành công' })
  store.setDirty()
  form.resetForm({})
})
</script>
<template>
  <AttributeValueForm
    :pageTitle="'Chỉnh sửa: ' + oldAttrValue.name"
    @submit="submit"
    :form="form"
  />
</template>
