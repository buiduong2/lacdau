<script setup lang="ts">
const categories = useCategoryStore()
await categories.fetch()
</script>
<template>
	<NuxtLayout>
		<NuxtPage />
	</NuxtLayout>
</template>
