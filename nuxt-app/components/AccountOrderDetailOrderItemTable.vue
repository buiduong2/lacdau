<script setup lang="ts">
import { Icon } from '@iconify/vue/dist/iconify.js'

defineProps<{ order: OrderDetailRes }>()
</script>
<template>
	<div class="order-detail-table-wrapper">
		<table class="order-detail-table">
			<thead class="table__header">
				<tr class="table__row">
					<th class="table__cell detail__img">Hình ảnh</th>
					<th class="table__cell detail__info">Sản phẩm</th>
					<th class="table__cell detail__price">Đơn giá</th>
					<th class="table__cell detail__ordered-quantity">SL cung cấp</th>
					<th class="table__cell detail__supplied-quantity">SL cung cấp</th>
					<th class="table__cell detail__total-price">Tổng giá</th>
				</tr>
			</thead>
			<tbody class="table__body">
				<tr
					class="table__row"
					v-for="item in order.orderItems"
					:key="item.product.productCode"
				>
					<td class="table__cell detail__img">
						<div class="img">
							<img
								v-if="item.product.mainImage"
								:src="item.product.mainImage.src"
								:alt="item.product.mainImage.alt"
							/>
							<img
								v-else
								src="/imgs/product-placeholder.png"
								:alt="item.product.name"
							/>
						</div>
					</td>
					<td class="table__cell detail__info">
						<h4 class="detail__title">
							<NuxtLink
								:to="`/product/${item.product.productCode}`"
								>{{ item.product.name }}</NuxtLink
							>
						</h4>
						<p class="detail__id">
							<Icon icon="fa6-solid:circle" /> Mã sản phẩm:
							{{ item.product.productCode }}
						</p>
						<p class="detail__msg">
							<span
								class="text-danger"
								v-if="item.suppliedQuantity === 0"
								>Không có mặt hàng</span
							>
							<span
								class="text-warning"
								v-else-if="
									item.requestedQuantity >
									item.suppliedQuantity
								"
								>Sản phẩm tồn kho không đủ cung cấp</span
							>
							<span
								class="text-success"
								v-else-if="
									item.requestedQuantity ===
									item.suppliedQuantity
								"
								>Cung cấp đầy đủ</span
							>
						</p>
					</td>
					<td class="table__cell">
						<span class="text-secondary">{{
							formatCurrencyVND(item.unitPrice)
						}}</span>
					</td>
					<td class="table__cell">{{ item.requestedQuantity }}</td>
					<td class="table__cell">{{ item.suppliedQuantity }}</td>
					<td class="table__cell detail__total-price">
						<span class="text-primary">
							{{
								formatCurrencyVND(
									item.unitPrice * item.suppliedQuantity
								)
							}}
						</span>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</template>

<style scoped></style>
