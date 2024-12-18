<script setup lang="ts">
import type { RouteLocationRaw } from 'vue-router'

const props = defineProps<{
	product: ProductSummaryRes
	isHorizontal?: boolean
}>()
const isAvaiable = computed(() => props.product.quantity > 0)

// const salePercentage = computedDiscountPercent(props.product)

const nuxtTo: RouteLocationRaw = {
	name: 'product-id',
	params: { id: props.product.id }
}
</script>

<template>
	<article
		class="product-item"
		:class="{ 'product-item--horizontal': props.isHorizontal }"
	>
		<div class="product-header">
			<NuxtLink data-test="product-hrefs" :to="nuxtTo">
				<img
					v-if="product.mainImage"
					:src="product.mainImage.src"
					:alt="product.mainImage.alt"
					data-test="product-img"
				/>
				<ClientOnly v-else>
					<img
						data-test="product-img"
						:src="`https://picsum.photos/200/200?random=${Math.random()}`"
					/>
				</ClientOnly>
			</NuxtLink>
		</div>

		<div class="product-body">
			<div class="product-meta">
				<p class="product-id" data-test="product-id">
					Mã: {{ product.id }}
				</p>
				<p
					class="product-state"
					:class="{ 'state--out': !isAvaiable }"
					data-test="product-state"
				>
					{{ isAvaiable ? 'Còn hàng' : 'Hết hàng' }}
				</p>
			</div>

			<h3 class="product-title" data-test="product-title">
				<NuxtLink data-test="product-hrefs" :to="nuxtTo">{{
					product.name
				}}</NuxtLink>
			</h3>
		</div>
		<div class="product-footer">
			<div class="product-price" v-if="product.salePrice">
				<p data-test="product-old-price" class="price--old">
					{{ product.originalPrice }}đ
				</p>
				<p class="price--primary" data-test="product-active-price">
					{{ product.salePrice }}đ
				</p>
			</div>
			<div v-else class="product-price">
				<p class="price--primary" data-test="product-active-price">
					{{ product.originalPrice }}đ
				</p>
			</div>

			<button class="product-action" data-test="product-cart-btn">
				<i class="product-cart"></i>
			</button>
		</div>
	</article>
</template>

<style lang="scss">
@use '@/assets/scss/components/product';
</style>
