<script setup lang="ts"></script>
<template>
  <AppFormGroupToggle
    title="Thuộc tính con"
    desc="Thêm các thuộc tính con tương ứng cho nhóm thuộc tính"
    default-open
  >
    <AppFormFieldArray
      name="attributeValues"
      label="Tên thuộc tính con"
      v-slot="componentField"
      :prototype-item="{ name: '' }"
    >
      <AppFormFieldInput
        :name="componentField.name + `.name`"
        placeholder="Thuộc tính con A"
        is-required
        type="text"
      />
    </AppFormFieldArray>
  </AppFormGroupToggle>
</template>
