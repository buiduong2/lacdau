<script setup lang="ts">
const props = defineProps<{
	products: ProductSummaryRes[]
	viewMode: 'grid' | 'list'
}>()
defineEmits<{ (e: 'addToCart', payload: ProductSummaryRes): void }>()
</script>
<template>
	<div class="product-list" v-if="products.length">
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
					@add-to-cart="product => $emit('addToCart', product)"
					:product="product"
					:is-horizontal="props.viewMode === 'list'"
				/>
			</div>
		</div>
	</div>
	<div class="empty-list" v-else>
		Hiện tại chưa có sản phẩm nào. bạn có thể đến Danh mục

		<NuxtLink :to="{ name: 'category-id', params: { id: 2 } }"
			>GAMING GEAR</NuxtLink
		>
		Để xem
	</div>
</template>
<style scoped>
.empty-list {
	min-height: 200px;
	font-size: 20px;
	display: flex;
	justify-content: center;
	align-items: center;
	gap: 10px;
}

.empty-list a {
	font-weight: 700;
	color: green;
	text-decoration: underline;
}
</style>
