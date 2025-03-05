<template>
  <FormField :name="name" v-slot="{ setValue, value }">
    <FormItem class="flex flex-row items-center justify-between rounded-lg border p-4">
      <div class="space-y-0.5">
        <FormLabel class="text-base">
          <span class="flex gap-3 items-center">
            {{ label }}
            <Icon v-if="icon" :icon="icon" class="size-5" />
          </span>
        </FormLabel>
        <FormDescription>
          {{ desc }}
        </FormDescription>
        <slot />
      </div>
      <FormControl>
        <Switch :checked="Boolean(value)" @update:checked="setValue" />
      </FormControl>
      <FormMessage />
    </FormItem>
  </FormField>
</template>

<script setup lang="ts">
import { FormField } from '@/components/ui/form'
defineProps<{ label: string; desc: string; name: string; icon?: string }>()
</script>

<style scoped></style>
