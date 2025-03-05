<!-- Chứa Datable Component của chúng ta -->
<script setup lang="ts">
import type { TableClientDropdown } from './types'
defineProps<
  {
    row: any
  } & TableClientDropdown
>()
</script>

<template>
  <DropdownMenu>
    <DropdownMenuTrigger as-child>
      <Button variant="ghost" class="w-8 h-8 p-0">
        <span class="sr-only">Open menu</span>
        <Icon icon="lucide:more-horizontal" class="w-4 h-4" />
      </Button>
    </DropdownMenuTrigger>
    <DropdownMenuContent align="end">
      <template v-for="(group, i1) in action.dropdowns" :key="i1">
        <DropdownMenuSeparator v-if="i1 != 0" />
        <template v-for="(item, i2) in group" :key="i2">
          <DropdownMenuItem
            class="cursor-pointer"
            :class="{ 'text-red-500': item.color === 'red' }"
            @click="item.click(row.id)"
          >
            <Icon :icon="item.icon" /> {{ item.text }}
          </DropdownMenuItem>
        </template>
      </template>
    </DropdownMenuContent>
  </DropdownMenu>
</template>
