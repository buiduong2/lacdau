<template>
  <Suspense>
    <RouterView />
  </Suspense>
</template>

<script setup lang="ts">
import { Authority } from '@/types/auth/resTypes'

definePage({ meta: { breadcrumb: 'Khách hàng', authority: Authority.CUSTOMER_MANAGE } })
</script>

<style scoped></style>
