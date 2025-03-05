<script setup lang="ts">
import { useField } from 'vee-validate';
import type { FormFieldProps } from './type';

const props = defineProps<FormFieldProps>()
const { setValue, value: fieldValue } = useField(props.name);
const value = ref<Date | undefined>(fieldValue.value as Date);


watch([value], () => {
  setValue(value.value);
},)
</script>
<template>
  <div class="space-y-2">
    <FormItem class="space-y-3 flex flex-col">
      <FormLabel v-if="label">
        {{ label }}
        <span v-if="isRequired" class="text-destructive">*</span>
      </FormLabel>
      <FormControl>

        <AppDateTimePicker v-model="value" />

      </FormControl>
      <FormDescription v-if="desc">{{ desc }} </FormDescription>
      <FormMessage />
    </FormItem>
  </div>
</template>



<style scoped></style>
