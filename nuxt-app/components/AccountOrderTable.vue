<script setup lang="ts">
defineProps<{ orders: OrderSummaryRes[] }>()

function getOrderStatusBadage(order: OrderSummaryRes) {
	const status = order.stage
	if (status === 'PENDING') {
		return 'badge-primary'
	}
	if (status === 'COMPLETED') {
		return 'badge-success'
	}
	if (status === 'CANCELED') {
		return 'badge-danger'
	}
	return 'badge-secondary'
}

function getOrderPaymentBadge(order: OrderSummaryRes) {
	const status = order.payment.status
	if (status === 'PROCESSING') {
		return 'badge-primary'
	}
	if (status === 'PAID') {
		return 'badge-success'
	}
	if (status === 'REFUNDED') {
		return 'badge-danger'
	}
	return 'badge-secondary'
}
</script>
<template>
	<div class="order-table__wrapper">
		<table class="order-table">
			<thead class="order-table__header">
				<tr>
					<th class="order-table__cell order-table__id">ID:</th>
					<th class="order-table__cell order-table__total-item">
						Số lượng
					</th>
					<th class="order-table__cell order-table__totalPrice">
						Tổng giá trị
					</th>
					<th class="order-table__cell order-table__status">
						Trạng thái
					</th>
					<th class="order-table__cell order-table__payment-status">
						Thanh toán
					</th>
					<th class="order-table__cell order-table__created-at">
						Ngày tạo
					</th>
					<th class="order-table__cell order-table__updated-at">
						Ngày cập nhật
					</th>
					<th class="order-table__cell order-table__action">
						Hành dộng
					</th>
				</tr>
			</thead>
			<tbody>
				<tr
					class="order-table__row"
					v-for="order in orders"
					:key="order.id"
				>
					<td class="order-table__cell order-table__id">
						{{ order.id }}
					</td>
					<td class="order-table__cell order-table__total-item">
						{{ order.payment.totalItem }}
					</td>
					<td class="order-table__cell order-table__total-price">
						{{
							formatCurrencyVND(
								order.payment.totalPrice ||
									order.payment.subTotalPrice
							)
						}}
					</td>
					<td class="order-table__cell order-table__status">
						<span
							class="badge"
							:class="getOrderStatusBadage(order)"
							>{{ order.stage }}</span
						>
					</td>
					<td class="order-table__cell order-table__payment-status">
						<span
							class="badge"
							:class="getOrderPaymentBadge(order)"
							>{{ order.payment.status }}</span
						>
					</td>
					<td class="order-table__cell order-table__created-at">
						{{ formatDate(order.createdAt) }}
					</td>
					<td class="order-table__cell order-table__updated-at">
						{{ formatDate(order.updatedAt) }}
					</td>
					<td class="order-table__cell order-table__action">
						<NuxtLink
							:to="{
								name: 'account-order-id',
								params: { id: order.id }
							}"
							>Chi tiết
						</NuxtLink>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</template>

<style scoped></style>
