<script setup lang="ts">
import { Icon } from '@iconify/vue/dist/iconify.js'
const props = defineProps<{
	subCategories: Category[]
	mainCategory: Category
	products: Product[]
	interval?: number
	initialDelay?: number
}>()

const wrapperEl = ref<HTMLElement | null>(null)
const { next, prev, isAtEnd, isAtStart, stopCountDown, resetCountDown } =
	useGallerySlide(
		{
			totalItem: props.products.length,
			visibleItemCount: 5,
			wrapperEl: wrapperEl
		},
		{ initialDelay: props.initialDelay, slideInterval: props.interval }
	)
</script>
<template>
	<section class="section-product section">
		<div class="container">
			<div class="section-product-inner">
				<div class="section-title-group">
					<h2 class="seciton-title">{{ mainCategory.name }}</h2>
					<div class="section-action-list">
						<div class="cat-btn-list">
							<NuxtLink
								v-for="cat in subCategories"
								class="cat-btn-item"
								:to="cat.href"
							>
								{{ cat.name }}
							</NuxtLink>
						</div>
						<div class="action-item">
							<NuxtLink :to="mainCategory.href">
								XEM THÊM <i class="fa-solid fa-arrow-right"></i>
							</NuxtLink>
						</div>
					</div>
				</div>

				<div class="section-body">
					<div class="product-slide">
						<div
							class="product-list row gx-0"
							ref="wrapperEl"
							@mouseenter="stopCountDown"
							@mouseleave="resetCountDown"
						>
							<div
								class="col-1/5"
								v-for="product in products"
								:key="product.id"
							>
								<AppProduct :product="product" />
							</div>
						</div>
						<button
							class="slide-action action--left"
							@click="prev"
							:disabled="isAtStart"
						>
							<Icon icon="fa-chevron-left"></Icon>
						</button>
						<button
							class="slide-action action--right"
							@click="next"
							:disabled="isAtEnd"
						>
							<Icon icon="fa-chevron-right"></Icon>
						</button>
					</div>
				</div>
			</div>
		</div>
	</section>
</template>

<style scoped></style>
