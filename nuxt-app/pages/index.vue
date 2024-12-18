<script setup lang="ts">
import { banners } from '@/mockData/home'

defineProps()
const products = getProducts()
const slideSubCategories = getCategories()
const slideMainCategories = getMainCategory()
</script>

<template>
	<section class="section-hero section">
		<div class="container">
			<div class="row">
				<div class="col-25"></div>
				<div class="col-95">
					<div class="hero-banner">
						<div class="row">
							<div class="col-9">
								<HomeBannerSlide :slides="banners.main" />
							</div>
							<div class="col-3">
								<div class="banner-right">
									<div
										class="banner-item"
										v-for="right in banners.right"
										:key="right"
									>
										<a href="#">
											<img :src="`/imgs/${right}`" alt=""
										/></a>
									</div>
								</div>
							</div>
							<div class="col-12">
								<div class="banner-bottom">
									<div class="row">
										<div
											class="col-4"
											v-for="bottom in banners.bottom"
											:key="bottom"
										>
											<div class="banner-item">
												<a href="#">
													<img
														:src="`/imgs/${bottom}`"
														alt=""
													/>
												</a>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>

	<HomeProductSlide
		:products
		:subCategories="slideSubCategories"
		:main-category="slideMainCategories"
	/>
	<HomeProductSlide
		:products
		:subCategories="slideSubCategories"
		:main-category="slideMainCategories"
		:initial-delay="2000"
	/>
	<HomeProductSlide
		:products
		:subCategories="slideSubCategories"
		:main-category="slideMainCategories"
		:initial-delay="4000"
	/>
</template>

<style lang="scss">
@use '@/assets/scss/pages/home.scss';

.slide-fade-enter-active,
.slide-fade-leave-active {
	transition: opacity 0.6s ease;
}
.slide-fade-enter-from,
.slide-fade-leave-to {
	opacity: 0;
}
.slide-fade-enter-to {
	opacity: 1;
}
</style>
