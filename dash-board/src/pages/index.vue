<template>
  <div class="text-2xl">Hello World</div>
</template>

<script setup lang="ts">
const { isAuthorized } = useAuthStore()
const router = useRouter()

if (!isAuthorized) {
  router.push('/403')
}
</script>

<style scoped></style>
