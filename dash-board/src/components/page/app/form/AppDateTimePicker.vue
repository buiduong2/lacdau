<script setup lang="ts">
import { cn } from '@/lib/utils';
import {
  DateFormatter,
  type DateValue,
  fromDate,
  getLocalTimeZone,
  today
} from '@internationalized/date';

const props = withDefaults(defineProps<{ modelValue?: Date }>(), { modelValue: () => new Date() })
const emits = defineEmits<{ (e: 'update:modelValue', payload: Date): void }>();
const df = new DateFormatter('vi-VN', {
  year: "numeric",
  month: "2-digit",
  day: "2-digit",
  hour: "2-digit",
  minute: "2-digit",
  hour12: false,
})
const value = computed(() => props.modelValue);
const dateValue = ref(fromDate(props.modelValue, 'UTC')) as Ref<DateValue>

const isOpen = ref<boolean>(false);
const hours = Array.from({ length: 24 }, (_, i) => i)
const minutes = Array.from({ length: 61 }, (_, i) => i);


function onTimeChange(type: "hour" | "minute", value: string) {

  const newDate = new Date(props.modelValue);

  if (type === 'hour') {
    newDate.setHours(parseInt(value));
  } else if (type === 'minute') {
    newDate.setMinutes(parseInt(value));
  }
  emits('update:modelValue', newDate)

}

watch([dateValue], () => {
  const newDate = new Date(props.modelValue);
  newDate.setMonth(dateValue.value.month - 1);
  newDate.setFullYear(dateValue.value.year)
  newDate.setDate(dateValue.value.day);
  emits('update:modelValue', newDate)
})

</script>

<template>
  <Popover v-model:open="isOpen">
    <PopoverTrigger as-child>
      <Button variant="outline" :class="cn(
        'w-[280px] justify-start text-left font-normal',
        !modelValue && 'text-muted-foreground',
      )">
        <Icon icon="radix-icons:calendar" class="mr-2 h-4 w-4" />
        {{ modelValue ? df.format(modelValue) :
          "Pick a date" }}
      </Button>
    </PopoverTrigger>

    <PopoverContent class="w-auto p-0">
      <div class="flex gap-5">
        <Calendar mode="single" v-model="dateValue" initialFocus />
        <div class="flex max-h-[300px] bg-popover">
          <ScrollArea class="border ">
            <div class="flex sm:flex-col p-1">
              <Button v-for="hour in hours" :key="hour" :variant="value.getHours() === hour ? 'default' : 'ghost'"
                class="px-1 shrink-0 aspect-square" type="button" @click="onTimeChange('hour', String(hour))">
                {{ hour }}
              </Button>
            </div>
            <ScrollBar orientation="horizontal" class="sm:hidden" />
          </ScrollArea>
          <ScrollArea class="border">
            <div class="flex sm:flex-col p-1">
              <Button v-for="minute in minutes" :key="minute" size="icon"
                :variant="modelValue.getMinutes() === minute ? 'default' : 'ghost'" class="shrink-0 px-1 aspect-square"
                type="button" @click="onTimeChange('minute', String(minute))">
                {{ minute }}
              </Button>
            </div>
            <ScrollBar orientation="horizontal" class="sm:hidden" />
          </ScrollArea>
        </div>
      </div>
    </PopoverContent>
  </Popover>
</template>


<style scoped></style>
