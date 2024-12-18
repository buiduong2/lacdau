<template>
  <Card>
    <CardHeader>
      <CardTitle>{{ title }}</CardTitle>
      <CardDescription class="whitespace-pre-line">{{ desc }} </CardDescription>
    </CardHeader>
    <CardContent>
      <div class="grid gap-6">
        <slot></slot>
      </div>
    </CardContent>
  </Card>
</template>

<script setup lang="ts">
import type { FormGroupProps } from './type'

defineProps<FormGroupProps>()
</script>

<style scoped></style>
