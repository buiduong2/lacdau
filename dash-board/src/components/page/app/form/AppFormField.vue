<template>
  <FormField v-slot="{ componentField }" :name="name">
    <FormItem class="w-full">
      <FormLabel v-if="label"
        >{{ label }} <span v-if="isRequired" class="text-destructive">*</span>
      </FormLabel>
      <FormControl>
        <slot v-bind="componentField"></slot>
      </FormControl>
      <FormDescription v-if="desc">{{ desc }} </FormDescription>
      <FormMessage />
    </FormItem>
  </FormField>
</template>

<script setup lang="ts">
import { FormField } from '@/components/ui/form'
import type { FormFieldProps } from './type'

defineProps<FormFieldProps>()
</script>

<style scoped></style>
