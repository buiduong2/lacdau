<script setup lang="ts">
const categories = useCategoryStore()
await categories.fetch()
const authStore = useAuthStore()
await authStore.init()
</script>
<template>
	<NuxtLayout>
		<NuxtPage />
	</NuxtLayout>
</template>
