<script setup lang="ts">
import type { FormlayoutProps } from './type'

defineProps<FormlayoutProps>()
</script>

<template>
  <div class="h-full flex-1 flex-col space-y-8 p-8">
    <div class="flex items-center gap-4">
      <Button variant="outline" size="icon" class="h-7 w-7" type="button" @click="$router.back()">
        <Icon icon="lucide:chevron-left" class="h-4 w-4" />
        <span class="sr-only">back</span>
      </Button>
      <h1 class="flex-1 shrink-0 whitespace-nowrap text-xl font-semibold tracking-tight sm:grow-0">
        {{ title }}
      </h1>
    </div>

    <div class="grid gap-4 md:grid-cols-[1fr_250px] lg:grid-cols-3 lg:gap-8">
      <div class="grid auto-rows-max items-start gap-4 lg:col-span-2 lg:gap-8">
        <slot></slot>
      </div>
    </div>
  </div>
</template>
