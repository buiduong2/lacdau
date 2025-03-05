<script setup lang="ts">
import { Button } from '@/components/ui/button'

import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover'
import { cn } from '@/lib/utils'
import {
  DateFormatter,
  type DateValue,
  getLocalTimeZone,
  fromDate

} from '@internationalized/date'
import { useField } from 'vee-validate'
import { ref } from 'vue'
import AppDatePicker from './AppDatePicker.vue'
import type { FormFieldProps } from './type'

const df = new DateFormatter('vi-VN', {
  dateStyle: 'long',
})

const props = defineProps<FormFieldProps>()

const { setValue, value: fieldValue } = useField(props.name);
const value = ref<DateValue | undefined>(fieldValue.value ? fromDate(fieldValue.value as Date, 'UTC') : undefined);


watch([value], () => {
  setValue(value.value?.toDate(getLocalTimeZone()))
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

        <Popover>
          <PopoverTrigger as-child>
            <Button variant="outline" :class="cn(
              'w-[280px] justify-start text-left font-normal',
              !value && 'text-muted-foreground',
            )">
              <Icon icon="radix-icons:calendar" class="mr-2 h-4 w-4" />
              {{ value ? df.format(value.toDate(getLocalTimeZone())) :
                "Pick a date" }}
            </Button>
          </PopoverTrigger>
          <PopoverContent class="w-auto p-0">
            <AppDatePicker v-model="value" />
          </PopoverContent>
        </Popover>

      </FormControl>
      <FormDescription v-if="desc">{{ desc }} </FormDescription>
      <FormMessage />
    </FormItem>
  </div>
</template>
