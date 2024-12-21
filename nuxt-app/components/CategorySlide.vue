<script setup lang="ts">
import { Icon } from '@iconify/vue/dist/iconify.js'
const props = defineProps<{ categories: Category[] }>()
const wrapperEl = ref<HTMLElement | null>(null)
const { next, prev, isAtEnd, isAtStart, onDrag, onDragStart, onDragEnd } =
	useGallerySlide(
		{
			totalItem: props.categories.length,
			visibleItemCount: 5,
			wrapperEl: wrapperEl
		},
		{
			autoSlide: false
		}
	)
</script>
<template>
	<div class="category-list">
		<div
			class="category-list-inner"
			@drag="onDrag"
			@dragstart="onDragStart"
			@dragend="onDragEnd"
			ref="wrapperEl"
		>
			<article
				class="category-item"
				v-for="subCat in categories"
				:key="subCat.id"
			>
				<NuxtLink :to="subCat.href">
					<div class="category-img-wapper">
						<NuxtImg src="/imgs/cat-1.jpg" alt="" />
					</div>

					<h4 class="category-title">
						{{ subCat.name }}
					</h4>
				</NuxtLink>
			</article>
		</div>
		<div class="category-slide-action">
			<button
				class="slide-action action--left"
				@click="prev"
				:disabled="isAtStart"
			>
				<Icon icon="fa-chevron-left" />
			</button>
			<button
				class="slide-action action--right"
				@click="next"
				:disabled="isAtEnd"
			>
				<Icon icon="fa-chevron-right" />
			</button>
		</div>
	</div>
</template>

<style scoped></style>
