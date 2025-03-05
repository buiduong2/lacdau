<script setup lang="ts">
defineProps<{ order: OrderDetailRes }>()
</script>
<template>
	<table class="order-info-table" v-if="order.shipment" v-bind="$attrs">
		<tbody>
			<tr>
				<th>Thời gian giao hàng dự kiến</th>
				<td>
					{{ formatDate(order.shipment.estimatedDelivery) }}
				</td>
			</tr>
			<tr>
				<th>Phí giao hàng</th>
				<td>
					{{ formatCurrencyVND(order.shipment.shippingPrice) }}
				</td>
			</tr>
			<tr>
				<th>Thông tin theo dõi</th>
				<td>{{ order.shipment.trackingDetail }}</td>
			</tr>
			<tr>
				<th>Người vận chuyển</th>
				<td>{{ order.shipment.carrier }}</td>
			</tr>
			<tr>
				<th>Ngày tạo</th>
				<td>
					{{ formatDate(order.shipment.createdAt) }}
				</td>
			</tr>

			<tr>
				<th>Ngày sửa đổi</th>
				<td>
					{{ formatDate(order.shipment.updatedAt) }}
				</td>
			</tr>
		</tbody>
	</table>
	<p v-else class="body-msg--empty">Chưa có thông tin</p>
</template>

<style scoped></style>
