<script setup lang="ts">
import type { RouteLocationRaw } from 'vue-router'

const props = defineProps<{
	product: ProductSummaryRes
	isHorizontal?: boolean
}>()

defineEmits<{
	(e: 'addToCart', payload: ProductSummaryRes): void
}>()
const isAvaiable = computed<boolean>(() => props.product.quantity > 0)
const isMaxQuantityReached = computed<boolean>(
	() =>
		props.product.orderedQuantity !== undefined &&
		props.product.orderedQuantity >= props.product.quantity
)

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
				<NuxtImg
					v-if="product.mainImage"
					:src="product.mainImage.src"
					:alt="product.mainImage.alt"
					data-test="product-img"
				/>
				<NuxtImg
					v-else
					src="/imgs/product-placeholder.png"
					:alt="product.name"
				/>
			</NuxtLink>
		</div>

		<div class="product-body">
			<div class="product-meta">
				<p class="product-id" data-test="product-id">
					Mã: {{ product.id }}
				</p>
				<p
					class="product-state"
					:class="{
						'state--out': !isAvaiable || isMaxQuantityReached
					}"
					data-test="product-state"
				>
					<span v-if="!isAvaiable">Hết hàng</span>
					<span v-else-if="isMaxQuantityReached">Đã thêm tối đa</span>
					<span v-else>Còn hàng</span>
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
					{{ formatCurrencyVND(product.originalPrice) }}
				</p>
				<p class="price--primary" data-test="product-active-price">
					{{ formatCurrencyVND(product.salePrice) }}
				</p>
			</div>
			<div v-else class="product-price">
				<p class="price--primary" data-test="product-active-price">
					{{ formatCurrencyVND(product.originalPrice) }}
				</p>
			</div>

			<button
				class="product-action"
				data-test="product-cart-btn"
				:disabled="!isAvaiable || isMaxQuantityReached"
				@click.prevent="$emit('addToCart', product)"
			>
				<i class="product-cart"></i>
			</button>
		</div>
	</article>
</template>

<style lang="scss">
@use '@/assets/scss/components/product';
</style>
