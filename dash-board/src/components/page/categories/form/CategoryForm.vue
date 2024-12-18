<template>
  <AppFormWithImageLayout :btn-title="btnTitle" :title="layoutTitle" :onSubmit="onSubmit">
    <AppFormGroup title="Tổng quan" desc="Điền thông tin tổng quan về Danh mục">
      <AppFormFieldInput
        name="name"
        is-required
        label="Tên"
        desc="Tên của danh mục"
        placeholder="Danh mục A"
        type="text"
      />
    </AppFormGroup>
    <AppFormGroupToggle
      title="Chọn danh mục cha"
      :desc="'Các sản phảm thuộc danh mục con cháu, hoặc bản thân danh mục trong trang danh sách sản phẩm sẽ được hiển thị kèm \n Tổng độ sâu không vượt quá 3'"
      :default-open="Boolean(parentId)"
    >
      <AppFormSelectCategory
        field-name="parentId"
        :form="form"
        :shoul-display-tree="shoulDisplayTree"
      />
    </AppFormGroupToggle>

    <template #right>
      <AppFormImage field-name="image" :src="src" />
    </template>
  </AppFormWithImageLayout>
</template>

<script setup lang="ts">
import type { CategoryTree } from '@/stores/category'
import type { FormContext } from 'vee-validate'

defineProps<{
  parentId?: number
  src?: string
  shoulDisplayTree: (cat: CategoryTree) => boolean
  btnTitle: string
  layoutTitle: string
  onSubmit: (payload: Event) => void
  form: FormContext
}>()
</script>

<style scoped></style>
