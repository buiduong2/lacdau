<template>
  <Suspense>
    <RouterView />
  </Suspense>
</template>

<script setup lang="ts">
import { Authority } from '@/types/auth/resTypes'
definePage({
  meta: {
    breadcrumb: 'Thương Hiệu',
    authority: Authority.PRODUCT_MANAGE,
  },
})
</script>

<style scoped></style>
