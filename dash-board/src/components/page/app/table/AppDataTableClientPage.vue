<script setup lang="ts">
import type { TablePageEmits, TablePageProps } from './types'

defineProps<TablePageProps>()

defineEmits<TablePageEmits>()
</script>

<template>
  <div class="h-full flex-1 flex-col space-y-8 p-8 md:flex">
    <div class="flex items-center justify-between space-y-2">
      <div>
        <h2 class="text-2xl font-bold tracking-tight">{{ heading }}</h2>
        <p class="text-muted-foreground">{{ description }}</p>
      </div>
      <Button
        class="text-base w-[180px]"
        variant="default"
        @click="$router.push({ name: createRouteName })"
      >
        <Icon icon="lucide:circle-plus" />Tạo mới</Button
      >
    </div>
    <AppDataTableClient
      :table="table"
      :filter-inputs="filterInputs"
      :filter-picks="filterPicks"
      @remove-by-id="(payload) => $emit('removeById', payload)"
      @edit-by-id="(payload) => $router.push({ name: updateRouteName, params: { id: payload } })"
    />
  </div>
</template>
