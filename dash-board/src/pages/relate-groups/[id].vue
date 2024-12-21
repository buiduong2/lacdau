<script setup lang="ts">
import { schema } from '@/components/page/relate-groups/form/schema'
import { toast } from '@/components/ui/toast'
import { toTypedSchema } from '@vee-validate/zod'
import { useForm } from 'vee-validate'
definePage({
  meta: { breadcrumb: ['Nhóm sản phẩm', 'Chỉnh sửa'] },
})
const groupId = Number(useRoute('/relate-groups/[id]').params.id)

const oldGroup = await fetchRelateGroup(groupId)

const form = useForm({
  validationSchema: toTypedSchema(schema),
  initialValues: {
    name: oldGroup.name,
  },
})

const store = useRelateStore()

const onSubmit = form.handleSubmit(async (formValue) => {
  await fetchRelateGroupUpdate(formValue, groupId)
  store.setDirty()
  toast({ description: 'Cập nhật thành công' })
})
</script>
<template>
  <RelateGroupForm @submit="onSubmit" :group-id="groupId" />
</template>

<style scoped></style>
