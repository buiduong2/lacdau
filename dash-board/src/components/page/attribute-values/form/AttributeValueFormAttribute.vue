<script setup lang="ts">
import type { CommandItem } from '../../app/form/type'

const store = useAttributeStore()
await store.fetchInit()
const comamndList: CommandItem[] = store.attributes.map((attr) => ({
  label: attr.name,
  value: attr.id,
}))
</script>

<template>
  <AppFormCombobox
    :command-list="comamndList"
    name="attributeId"
    command-place-holder="Chọn nhóm thuộc tính"
    place-holder="Điền vào tên"
    label="Attribute"
  />
</template>
