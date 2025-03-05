<script setup lang="ts">
const authStore = useAuthStore()
</script>

<template>
  <AppLayoutEmpty v-if="!authStore.isAuthenticated || !authStore.isAuthorized">
    <RouterView v-slot="{ Component, route }">
      <Suspense v-if="Component" :timeout="500">
        <Component :is="Component" :key="route.name" />
        <template #fallback> Loading...</template>
      </Suspense>
    </RouterView>
  </AppLayoutEmpty>

  <AppLayout v-else>
    <RouterView v-slot="{ Component, route }">
      <Suspense v-if="Component" :timeout="500">
        <Component :is="Component" :key="route.name" />
        <template #fallback> Loading...</template>
      </Suspense>
    </RouterView>
  </AppLayout>
  <Toaster />
</template>

<style scoped></style>
