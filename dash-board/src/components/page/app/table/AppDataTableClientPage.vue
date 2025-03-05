<script setup lang="ts">
import type { TablePageProps } from './types'

defineProps<TablePageProps>()
</script>

<template>
  <div class="h-full flex-1 flex-col space-y-8 p-8 md:flex">
    <div class="flex items-center justify-between space-y-2">
      <div>
        <h2 class="text-2xl font-bold tracking-tight">{{ heading }}</h2>
        <p class="text-muted-foreground">{{ description }}</p>
      </div>
      <Button class="text-base w-[180px]" variant="default" @click="$router.push(action.createUrl)">
        <Icon icon="lucide:circle-plus" />Tạo mới</Button
      >
    </div>
    <AppDataTableClient
      :action="action"
      :table="table"
      :filter-inputs="filterInputs"
      :filter-picks="filterPicks"
    />
  </div>
</template>
