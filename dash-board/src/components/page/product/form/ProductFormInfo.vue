<script setup lang="ts">
import type { CommandItem } from '../../app/form/type'
import type { FormContext } from 'vee-validate'
import type { TypeSchema } from './schema'

defineProps<{ form: FormContext<TypeSchema> | FormContext }>()

const productCodeStore = useProductCodeStore()
await productCodeStore.fetchInit()
const codes: CommandItem[] = productCodeStore.codes.map((code) => ({
  value: code.name,
  label: 'Nhóm' + code.name,
}))
</script>
<template>
  <AppFormCombobox
    name="prefixProductCode"
    :command-list="codes"
    place-holder="Chọn Code"
    command-place-holder="Điền code..."
    label="Code"
    is-required
  />
  <AppFormFieldInput name="name" label="Tên" type="text" placeholder="Sản phẩm A" is-required />
  <AppFormFieldInput
    type="number"
    name="originalPrice"
    placeholder="10000"
    label="Giá gốc"
    is-required
  />
  <AppFormFieldInput type="number" name="salePrice" placeholder="90000" label="Giá khuyến mại" />
  <AppFormFieldInput type="number" name="quantity" placeholder="100" label="Số lượng" is-required />
  <AppFormFieldArray label="Thông số sản phẩm" name="detail.specifications" desc="" />
</template>
