<script setup lang="ts">
import { Icon } from '@iconify/vue/dist/iconify.js'
interface Image {
	src: string
	alt: string
}
const index = ref<number>(0)
const images: Image[] = [
	{ src: '/imgs/product-detail-1.jpg', alt: '' },
	{ src: '/imgs/product-detail-1.jpg', alt: '' },
	{ src: '/imgs/product-detail-1.jpg', alt: '' },
	{ src: '/imgs/product-detail-1.jpg', alt: '' },
	{ src: '/imgs/product-detail-1.jpg', alt: '' }
]
const pickedImage = computed<Image | undefined>(() => images[index.value])
const wrapperEl = ref()

const { isAtEnd, isAtStart, next, onDrag, onDragEnd, onDragStart, prev } =
	useGallerySlide(
		{
			totalItem: images.length,
			visibleItemCount: 4,
			wrapperEl: wrapperEl
		},
		{ autoSlide: false }
	)
</script>
<template>
	<div class="product-detail-img">
		<div class="detail-main-img">
			<img
				v-if="pickedImage"
				:src="pickedImage.src"
				:alt="pickedImage.alt"
			/>
		</div>

		<div class="detail-thumbnail-wrapper">
			<div class="detail-thumbnail-slide">
				<div
					class="detail-thumbnail-list row gx-2"
					ref="wrapperEl"
					@drag="onDrag"
					@dragstart="onDragStart"
					@dragend="onDragEnd"
				>
					<div class="col-3" v-for="(image, i) in images" :key="i">
						<div
							class="detail-thumbnail-item"
							:class="{ active: index === i }"
							@click="index = i"
						>
							<NuxtImg :src="image.src" :alt="image.src" />
						</div>
					</div>
				</div>

				<div class="detail-thumbnail-action-list">
					<button
						class="thumbnail-action-item"
						:disabled="isAtStart"
						@click="prev"
					>
						<Icon icon="fa6-solid:chevron-left" />
					</button>
					<button
						class="thumbnail-action-item"
						:disabled="isAtEnd"
						@click="next"
					>
						<Icon icon="fa6-solid:chevron-right" />
					</button>
				</div>
			</div>
		</div>
	</div>
</template>

<style scoped></style>
