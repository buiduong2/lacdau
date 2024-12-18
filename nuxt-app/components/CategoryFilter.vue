<script setup lang="ts">
defineProps<{ filters: Filter[] }>()
</script>
<template>
	<aside class="product-filter-list">
		<CategoryFilterItem
			v-for="(filter, index) in filters"
			:key="filter.id + String(index)"
			:filter="filter"
		/>
	</aside>
</template>

<style scoped></style>
