<template>
  <Suspense>
    <RouterView></RouterView>
  </Suspense>
</template>

<script setup lang="ts">
import { Authority } from '@/types/auth/resTypes'
import { RouterView } from 'vue-router'

definePage({
  meta: {
    breadcrumb: 'Người dùng',
    authority: Authority.USER_MANAGE,
  },
})
</script>

<style scoped></style>
