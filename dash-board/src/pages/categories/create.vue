<script setup lang="ts">
import { schema } from '@/components/page/categories/form/schema'
import { toast } from '@/components/ui/toast'
import type { CategoryTree } from '@/stores/category'
import { toTypedSchema } from '@vee-validate/zod'
import { useForm } from 'vee-validate'

const form = useForm({
  validationSchema: toTypedSchema(schema),
})

const store = useCategoryStore()

const onSubmit = form.handleSubmit(async (formValue) => {
  await fetchCategoryCreate(formValue)
  store.setDirty()
  form.resetForm()
  toast({ description: 'Thêm Category thành công' })
})
</script>
<template>
  <CategoryForm
    :form="form"
    btn-title="Lưu danh mục"
    layout-title="Tạo Danh Mục mới"
    :shoul-display-tree="(cat: CategoryTree) => cat.level < 1"
    :onSubmit="onSubmit"
  />
</template>
