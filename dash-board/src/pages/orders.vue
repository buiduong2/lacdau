<script setup lang="ts">
import { Authority } from '@/types/auth/resTypes'

definePage({ meta: { breadcrumb: 'Đơn hàng', authority: Authority.ORDER_MANAGE } })
</script>
<template>
  <Suspense>
    <RouterView />
  </Suspense>
</template>

<style scoped></style>
