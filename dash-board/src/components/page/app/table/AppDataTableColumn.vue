<script setup lang="ts">
import type { Column } from '@tanstack/vue-table'
import { cn } from '@/lib/utils'

interface DataTableColumnHeaderProps {
  column: Column<any, any>
  title: string
}

defineProps<DataTableColumnHeaderProps>()
</script>

<template>
  <div v-if="column.getCanSort()" :class="cn('flex items-center space-x-2', $attrs.class ?? '')">
    <DropdownMenu>
      <DropdownMenuTrigger as-child>
        <Button variant="ghost" size="sm" class="-ml-3 h-8 data-[state=open]:bg-accent">
          <span>{{ title }}</span>
          <Badge variant="secondary" v-if="column.getIsSorted()">{{
            column.getSortIndex() + 1
          }}</Badge>
          <Icon icon="lucide:arrow-down" v-if="column.getIsSorted() === 'desc'" class="w-4 h-4" />
          <Icon icon="lucide:arrow-up" v-else-if="column.getIsSorted() === 'asc'" class="w-4 h-4" />
          <Icon icon="radix-icons:caret-sort" v-else class="w-4 h-4" />
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent align="start">
        <DropdownMenuItem @click="column.toggleSorting(false, true)">
          <Icon icon="lucide:arrow-up" class="mr-2 h-3.5 w-3.5 text-muted-foreground/70" />
          Tăng dần
        </DropdownMenuItem>
        <DropdownMenuItem @click="column.toggleSorting(true, true)">
          <Icon icon="lucide:arrow-down" class="mr-2 h-3.5 w-3.5 text-muted-foreground/70" />
          Giảm dần
        </DropdownMenuItem>
        <DropdownMenuSeparator />
        <DropdownMenuItem @click="column.clearSorting()">
          <Icon icon="radix-icons:caret-sort" class="mr-2 h-3.5 w-3.5 text-muted-foreground/70" />
          Bỏ sắp xếp
        </DropdownMenuItem>
        <DropdownMenuItem @click="column.toggleVisibility(false)">
          <Icon icon="radix-icons:eye-none" class="mr-2 h-3.5 w-3.5 text-muted-foreground/70" />
          Ẩn cột
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  </div>

  <div v-else :class="$attrs.class">
    {{ title }}
  </div>
</template>
