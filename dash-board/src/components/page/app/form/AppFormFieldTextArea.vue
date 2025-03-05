<template>
  <AppFormField :desc="desc" :is-required="isRequired" :label="label" :name="name" v-slot="componentField">
    <Textarea :placeholder="placeholder" v-bind="componentField"></Textarea>
  </AppFormField>
</template>

<script setup lang="ts">
import type { FormFieldTextAreaProps } from './type';

defineProps<FormFieldTextAreaProps>()
</script>

<style scoped></style>
