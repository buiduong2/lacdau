<script setup lang="ts">
import type { FormContext } from 'vee-validate'
import type { CommandItem } from '../../app/form/type'
defineProps<{ form: FormContext }>()

const store = useBrandStore()
await store.fetchInit()
const brands: CommandItem[] = store.brands.map((b) => ({ label: b.name, value: b.id }))
</script>

<template>
  <AppFormCombobox
    :command-list="brands!"
    command-place-holder="Điền vào tên thương hiệu"
    name="brandId"
    place-holder="Chọn một thương hiệu"
  />
</template>

<style scoped></style>
