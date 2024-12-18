<script setup lang="ts">
import type { ProductFull } from '@/types/products/resTypes'
import type { FormContext } from 'vee-validate'
import type { TypeSchema } from './schema'

defineProps<{
  form: FormContext<TypeSchema> | FormContext
  product?: ProductFull
  onSubmit: (e?: Event) => Promise<Promise<void> | undefined>
}>()
</script>
<template>
  <form class="mx-auto grid flex-1 auto-rows-max gap-4" @submit="onSubmit">
    <div class="flex items-center gap-4">
      <Button variant="outline" size="icon" class="h-7 w-7" type="button" @click="$router.back()">
        <Icon icon="lucide:chevron-left" class="h-4 w-4" />
        <span class="sr-only">back</span>
      </Button>
      <h1 class="flex-1 shrink-0 whitespace-nowrap text-xl font-semibold tracking-tight sm:grow-0">
        Tạo Product mới
      </h1>
      <Badge variant="outline" class="ml-auto sm:ml-0"> In stock </Badge>
      <div class="hidden items-center gap-2 md:ml-auto md:flex">
        <Button variant="outline" size="sm" type="reset"> Discard </Button>
        <Button size="sm" type="submit"> Save Product </Button>
      </div>
    </div>
    <div class="grid gap-4 md:grid-cols-[1fr_250px] lg:grid-cols-3 lg:gap-8">
      <div class="grid auto-rows-max items-start gap-4 lg:col-span-2 lg:gap-8">
        <AppFormGroup title="Tổng quan" desc="Nhập thông tin tổng quan về sản phẩm">
          <ProductFormInfo :form="form" />
        </AppFormGroup>
        <AppFormGroupToggle
          title="Nhóm sản phẩm"
          desc="Chọn một nhóm để hiển thị kèm trong trang chi tiết"
          :default-open="!!form.values.brandId"
        >
          <ProductFormRelate :form="form" :product-id="product?.id" />
        </AppFormGroupToggle>

        <AppFormGroupToggle
          title="Thuộc tính"
          desc="Chọn các thuộc tính hỗ trợ cho việc phân loại, tìm kiếm"
          :default-open="!!form.values.attributeValueIds"
        >
          <ProductFormAttribute :form="form" />
        </AppFormGroupToggle>
        <AppFormGroupToggle
          title="Danh mục"
          desc="Chọn danh mục cho sản phẩm, danh mục cấp thấp nhất sẽ được áp dụng"
          :default-open="!!form.values.categoryId"
        >
          <AppFormSelectCategory :form="form" field-name="categoryId" />
        </AppFormGroupToggle>

        <AppFormGroupToggle
          title="Brand"
          desc="Chọn thương hiệu cho sản phẩm"
          :default-open="!!form.values.brandId"
        >
          <ProductFormBrand :form="form" />
        </AppFormGroupToggle>
      </div>

      <div class="grid auto-rows-max items-start gap-4 lg:gap-8">
        <ProductFormStatus />
        <AppFormImage field-name="mainImage" :src="product?.mainImage?.src" />
        <ProductFormThumbnail :product="product" />
      </div>
    </div>
  </form>
</template>
