<script setup lang="ts">
import { schema } from '@/components/page/attributes/form/updateSchema'
import { toast } from '@/components/ui/toast'
import { toTypedSchema } from '@vee-validate/zod'
import { useForm } from 'vee-validate'

definePage({
  meta: { breadcrumb: ['Thuộc tính', 'Chỉnh sửa'] },
})
const route = useRoute('/attributes/[id]')
const id = route.params.id
const oldAttribute = await fetchAttribute(id)
const store = useAttributeStore()
await store.fetchInit()

const oldAttributeValues = store.getAttributeValuesByIdIn(oldAttribute.attributeValueIds)
const form = useForm({
  validationSchema: toTypedSchema(schema),
  initialValues: {
    name: oldAttribute.name,
  },
})

const onSubmit = form.handleSubmit(async (formValue) => {
  await fetchAttributeUpdate(formValue, id)
  store.setDirty()
  toast({ description: 'Thêm Attribute thành công' })
  form.resetForm()
})
</script>
<template>
  <AppFormLayout :title="'Chỉnh sửa ' + oldAttribute.name">
    <form class="flex gap-4 flex-col" @submit="onSubmit">
      <AttributeFormInfo />

      <AttributeFormRemoveAttributeValue :old-attribute-values="oldAttributeValues" />

      <AttributeFormAddAttributeValue />

      <Button>Xác nhận</Button>
    </form>
  </AppFormLayout>
</template>
