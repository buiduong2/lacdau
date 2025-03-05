<script setup lang="ts">
import { schema } from '@/components/page/categories/form/schema'
import { toast } from '@/components/ui/toast'
import type { CategoryTree } from '@/stores/category'
import { toTypedSchema } from '@vee-validate/zod'
import { useForm } from 'vee-validate'

definePage({
  meta: { breadcrumb: 'Chỉnh sửa' },
})

const route = useRoute('/categories/[id]')
const id = Number(route.params.id)

const prototype = await fetchCategory(id)

const form = useForm({
  validationSchema: toTypedSchema(schema),
  initialValues: {
    name: prototype.name,
    parentId: prototype.parentId || undefined,
  },
})

const store = useCategoryStore()
await store.fetchInit()

const onSubmit = form.handleSubmit(async (formValue) => {
  await fetchCategoryEdit(formValue, id)
  store.setDirty()
  form.resetForm()
  toast({ description: 'Thêm Category thành công' })
})
</script>
<template>
  <CategoryForm
    :parent-id="prototype.parentId || undefined"
    :form="form"
    :src="prototype.imageSrc || undefined"
    btn-title="Lưu danh mục"
    :layout-title="'Chỉnh sửa : ' + prototype.name"
    :shoul-display-tree="
      (cat: CategoryTree) => cat.level + (store.findTreeById(prototype.id)?.heigth || 0) < 2
    "
    :onSubmit="onSubmit"
  />
</template>
