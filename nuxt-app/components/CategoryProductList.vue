<script setup lang="ts">
const props = defineProps<{ products: ProductSummaryRes[]; viewMode: 'grid' | 'list' }>()
</script>
<template>
	<div class="product-list">
		<div class="row gx-3 gy-3">
			<div
				:class="{
					'col-3': props.viewMode === 'grid',
					'col-6': props.viewMode === 'list'
				}"
				v-for="product in products"
				:key="product.id"
			>
				<AppProduct
					:product="product"
					:is-horizontal="props.viewMode === 'list'"
				/>
			</div>
		</div>
	</div>
</template>
<style scoped></style>
