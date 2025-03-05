<script setup lang="ts">
import { FormField } from '@/components/ui/form';
import type { FormFieldCheckboxProps } from './type';

defineProps<FormFieldCheckboxProps>();
</script>
<template>
  <FormField v-slot="{ handleChange, value }" type="checkbox" :name="name">
    <FormItem class="flex flex-row items-start space-x-3 space-y-0">
      <FormControl>
        <Checkbox :checked="value" @update:checked="handleChange" />
      </FormControl>
      <div class="space-y-1 leading-none">
        <FormLabel class="cursor-pointer">
          {{ label }}
        </FormLabel>
        <FormDescription>
          {{ desc }}
        </FormDescription>
        <FormMessage />
      </div>
    </FormItem>
  </FormField>
</template>


<style scoped></style>
