<template>
  <Suspense>
    <RouterView />
  </Suspense>
</template>

<script setup lang="ts">
import { Authority } from '@/types/auth/resTypes'
definePage({
  meta: {
    breadcrumb: 'Nhóm sản phẩm',
    authority: Authority.PRODUCT_MANAGE,
  },
})
</script>

<style scoped></style>
