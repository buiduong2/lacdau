<script setup lang="ts">
const props = defineProps<{ slides: any[] }>()

const {
	activeSlide: activeMiddle,
	changeSlide,
	currentIndex
} = useFocusSlide(props.slides, {
	defaultIndex: 0,
	timeout: 3000
})
</script>
<template>
	<div class="banner-slide">
		<transition name="slide-fade">
			<div class="banner-item" :key="activeMiddle">
				<a href="#">
					<img :src="`/imgs/${activeMiddle}`" />
				</a>
			</div>
		</transition>

		<div class="banner-slide-action">
			<button
				class="action-item"
				:class="{
					active: index === currentIndex
				}"
				v-for="(middle, index) in slides"
				:key="middle"
				@click="changeSlide(index)"
			></button>
		</div>
	</div>
</template>
<style scoped></style>
