<template>
  <AppFormField
    :desc="desc"
    :is-required="isRequired"
    :label="label"
    :name="name"
    v-slot="componentField"
  >
    <Input :type="type" :placeholder="placeholder" v-bind="componentField" />
    <slot></slot>
  </AppFormField>
</template>

<script setup lang="ts">
import type { FormFieldInputProps } from './type'

defineProps<FormFieldInputProps>()
</script>

<style scoped></style>
