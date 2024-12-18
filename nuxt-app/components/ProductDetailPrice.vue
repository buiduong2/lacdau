<script setup lang="ts">
const props = defineProps<{
	salePrice: ProductDetailRes['salePrice']
	originalPrice: ProductDetailRes['originalPrice']
}>()

const saleable = { ...props }
</script>

<template>
	<div class="detail-body-price">
		<table v-if="salePrice">
			<tbody>
				<tr>
					<td class="price-label">Giá bán:</td>
					<td class="price-value--old">{{ originalPrice }}đ</td>
				</tr>
				<tr>
					<td class="price-label">Giá khuyến mại:</td>
					<td class="price-value--active">
						<span class="value">
							{{ salePrice ?? originalPrice }}đ
						</span>
						(Tiết kiệm
						{{ computedDiscountPercent(saleable) }}%)
					</td>
				</tr>
			</tbody>
		</table>
		<table v-else>
			<tbody>
				<tr>
					<td class="price-label">Giá bán:</td>
					<td class="price-value--active">
						<span class="value"> {{ originalPrice }}đ </span>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</template>

<style scoped></style>
