<template>
  <Popover v-model:open="open">
    <PopoverTrigger as-child>
      <Component :is="notForm ? 'div' : FormControl">
        <Button
          type="button"
          variant="outline"
          class="w-full text-center h-auto"
          role="combobox"
          :aria-expanded="open"
          :class="cn(' justify-between', !value && 'text-muted-foreground')"
        >
          <span class="text-balance">{{
            value ? commandList.find((item) => item.value === value)?.label : placeholder
          }}</span>

          <Icon icon="lucide:chevrons-up-down" class="ml-2 h-4 w-4 shrink-0 opacity-50" />
        </Button>
      </Component>
    </PopoverTrigger>
    <PopoverContent class="p-0">
      <Command>
        <CommandInput :placeholder="commandPlaceholder" />
        <CommandEmpty>No Item found.</CommandEmpty>
        <CommandList>
          <CommandGroup>
            <CommandItem
              v-for="item in commandList"
              :key="item.value"
              :value="item.label"
              @select="
                () => {
                  $emit('selectItem', item)
                  open = false
                }
              "
            >
              <Icon
                icon="lucide:check"
                :class="cn('mr-2 h-4 w-4', value === item.value ? 'opacity-100' : 'opacity-0')"
              />
              {{ item.label }}
            </CommandItem>
          </CommandGroup>
        </CommandList>
      </Command>
    </PopoverContent>
  </Popover>
</template>

<script setup lang="ts">
import { cn } from '@/lib/utils'
import type { CommandItem } from './type'
import { FormControl } from '@/components/ui/form'

const open = ref(false)
defineProps<{
  value: any
  commandList: CommandItem[]
  commandPlaceholder: string
  placeholder: string
  notForm?: boolean
}>()

defineEmits<{
  (e: 'selectItem', payload: CommandItem): void
}>()
</script>

<style scoped></style>
