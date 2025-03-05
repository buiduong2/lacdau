<template>
  <FormField v-slot="{ componentField }" type="radio" :name="name">
    <FormItem class="space-y-3">
      <FormLabel v-if="label">
        {{ label }}
        <span v-if="isRequired" class="text-destructive">*</span>
      </FormLabel>

      <FormControl>
        <RadioGroup class="flex  space-x-1" v-bind="componentField">
          <FormItem class="flex items-center space-y-0 gap-x-3" v-for="option in options" :key="option.value">
            <FormControl>
              <RadioGroupItem :value="option.value" />
            </FormControl>
            <FormLabel class="font-normal">
              {{ option.label }}
            </FormLabel>
          </FormItem>
        </RadioGroup>
      </FormControl>
      <FormMessage />
      <FormDescription v-if="desc">{{ desc }} </FormDescription>
    </FormItem>
  </FormField>
</template>

<script setup lang="ts">
import { FormField } from '@/components/ui/form';
import type { FormFieldRadioProps } from './type';
defineProps<FormFieldRadioProps>()

</script>

<style scoped></style>
