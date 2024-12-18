<script setup lang="ts">
import { FormField } from '@/components/ui/form'
import type { FromFieldComboboxProps } from './type'
defineProps<FromFieldComboboxProps>()
</script>
<template>
  <FormField v-slot="{ value, setValue }" :name="name">
    <FormItem class="flex flex-col">
      <FormLabel v-if="label">
        {{ label }} <span v-if="isRequired" class="text-destructive">*</span>
      </FormLabel>

      <AppCombobox
        :command-list="commandList"
        :placeholder="placeHolder"
        :value="value"
        :command-placeholder="commandPlaceHolder"
        @select-item="(item) => setValue(item.value)"
      />

      <FormDescription v-if="desc"> {{ desc }}</FormDescription>
      <FormMessage />
    </FormItem>
  </FormField>
</template>
