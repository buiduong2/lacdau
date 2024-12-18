<script setup lang="ts">
import { cn } from '@/lib/utils'
import type { Column } from '@tanstack/vue-table'
import type { TableFilterOption } from './types'

interface DataTableFacetedFilter {
  column: Column<any, any>
  title?: string
  options: TableFilterOption[]
}

const props = defineProps<DataTableFacetedFilter>()
const facets = computed(() => props.column?.getFacetedUniqueValues())
const selectedValues = computed(
  () => new Set<String | null | number>(props.column?.getFilterValue() as String[]),
)
</script>
<template>
  <Popover>
    <PopoverTrigger as-child>
      <Button variant="outline">
        <Icon icon="lucide:circle-plus" />
        {{ title }}

        <template v-if="selectedValues.size > 0">
          <Separator orientation="vertical" class="mx-2 h4" />
          <Badge variant="secondary" class="rounded-sm px-1 font-normal">
            {{ selectedValues.size }}
          </Badge>
          <div class="space-x-1 flex">
            <Badge
              v-if="selectedValues.size > 2"
              variant="secondary"
              class="rounded-sm px-1 font-normal"
            >
              {{ selectedValues.size }} selected
            </Badge>
            <template v-else>
              <Badge
                v-for="option in options
                  .filter((option) => option.value)
                  .filter((option) => selectedValues.has(option.value!))"
                :key="option.value || 'null'"
                variant="secondary"
                class="rounded-sm px-1 font-normal"
              >
                {{ option.label }}
              </Badge>
            </template>
          </div>
        </template>
      </Button>
    </PopoverTrigger>
    <PopoverContent class="w-[200px] p-0" align="start">
      <Command>
        <CommandInput :placeholder="title" />
        <CommandList>
          <CommandEmpty>No results found.</CommandEmpty>
          <CommandGroup>
            <CommandItem
              v-for="option in options"
              :key="option.value || 'null'"
              :value="option.label"
              @select="
                (e) => {
                  const isSelected = selectedValues.has(option.value)
                  if (isSelected) {
                    selectedValues.delete(option.value)
                  } else {
                    selectedValues.add(option.value)
                  }
                  const filterValues = Array.from(selectedValues)
                  column?.setFilterValue(filterValues.length ? filterValues : undefined)
                }
              "
            >
              <div
                :class="
                  cn(
                    'mr-2 flex h-4 w-4 items-center justify-center rounded-sm border border-primary',
                    selectedValues.has(option.value)
                      ? 'bg-primary text-primary-foreground'
                      : 'opacity-50 [&_svg]:invisible',
                  )
                "
              >
                <Icon icon="lucide:check" class="h-4 w-4" />
              </div>
              <component
                :is="option.icon"
                v-if="option.icon"
                class="mr-2 h-4 w-4 text-muted-foreground"
              />
              <span>{{ option.label }}</span>
              <span
                v-if="facets?.get(option.value)"
                class="ml-auto flex h-4 w-4 items-center justify-center font-mono text-xs"
              >
                {{ facets.get(option.value) }}
              </span>
            </CommandItem>
          </CommandGroup>

          <template v-if="selectedValues.size > 0">
            <CommandSeparator />
            <CommandGroup>
              <CommandItem
                :value="{ label: 'Clear filters' }"
                class="justify-center text-center"
                @select="column?.setFilterValue(undefined)"
              >
                Clear filters
              </CommandItem>
            </CommandGroup>
          </template>
        </CommandList>
      </Command>
    </PopoverContent>
  </Popover>
</template>
<style scoped></style>
