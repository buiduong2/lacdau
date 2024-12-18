<script setup lang="ts">
import { Icon } from '@iconify/vue/dist/iconify.js'
</script>
<template>
	<div class="product-detail-img">
		<div class="detail-main-img">
			<img src="/assets/imgs/product-detail-1.jpg" alt="" />
		</div>

		<div class="detail-thumbnail-wrapper">
			<div class="detail-thumbnail-slide">
				<div class="detail-thumbnail-list row gx-2">
					<div class="col-3">
						<div class="detail-thumbnail-item active">
							<img
								src="/assets/imgs/product-detail-thumbnail-1.jpg"
								alt=""
							/>
						</div>
					</div>
					<div class="col-3">
						<div class="detail-thumbnail-item">
							<img
								src="/assets/imgs/product-detail-thumbnail-1.jpg"
								alt=""
							/>
						</div>
					</div>
					<div class="col-3">
						<div class="detail-thumbnail-item">
							<img
								src="/assets/imgs/product-detail-thumbnail-1.jpg"
								alt=""
							/>
						</div>
					</div>
					<div class="col-3">
						<div class="detail-thumbnail-item">
							<img
								src="/assets/imgs/product-detail-thumbnail-1.jpg"
								alt=""
							/>
						</div>
					</div>
					<div class="col-3">
						<div class="detail-thumbnail-item">
							<img
								src="/assets/imgs/product-detail-thumbnail-1.jpg"
								alt=""
							/>
						</div>
					</div>
				</div>

				<div class="detail-thumbnail-action-list">
					<button class="thumbnail-action-item" disabled>
						<Icon icon="fa6-solid:chevron-left" />
					</button>
					<button class="thumbnail-action-item">
						<Icon icon="fa6-solid:chevron-right" />
					</button>
				</div>
			</div>
		</div>
	</div>
</template>

<style scoped></style>
