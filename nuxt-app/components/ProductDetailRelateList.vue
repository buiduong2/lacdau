<script setup lang="ts">
import { Icon } from '@iconify/vue/dist/iconify.js'
const props = defineProps<{
	relatedProducts: NonNullable<ProductDetailRes['relatedProducts']>
	activeId: ProductDetailRes['id']
}>()

const isOverFlow = props.relatedProducts.length > 4
const isShow = ref(false)
</script>
<template>
	<div class="detail-item detail-body-related">
		<h3 class="detail-item-title">Tùy Chọn Sản Phẩm</h3>

		<ul
			class="related-list row gx-3 gy-3"
			data-show-id="relate-list"
			:class="{ show: isShow }"
		>
			<li
				class="col-3"
				v-for="relate in relatedProducts"
				:key="relate.id"
			>
				<NuxtLink
					class="related-link"
					:class="{
						active: relate.id === activeId
					}"
					:to="{
						name: 'product-id',
						params: {
							id: relate.id
						}
					}"
				>
					<div class="related-item">
						<p class="related-item-name">
							{{ relate.name }}
						</p>

						<p class="related-item-price">
							{{ relate.price }} đ

							
							<Icon
								class="related-icon"
								icon="fa6-solid:circle"
							/>
						</p>
					</div>
				</NuxtLink>
			</li>
		</ul>

		<div class="detail-item-action" v-if="isOverFlow">
			<button class="detail-item-action-btn" @click="isShow = !isShow">
				{{ isShow ? 'Thu gọn' : 'Xem thêm' }}
				<Icon
					class="detail-item-action-icon"
					:icon="
						isShow ? 'fa6-solid:up-long' : 'fa6-solid:right-long'
					"
				/>
			</button>
		</div>
	</div>
</template>

<style>
[data-show-id='relate-list'] > li:nth-child(n + 5) {
	display: none;
}
[data-show-id='relate-list'].show > li:nth-child(n + 5) {
	display: list-item;
}
</style>
