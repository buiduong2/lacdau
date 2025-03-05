<script setup lang="ts">
defineProps<{ order: OrderDetailRes }>()
</script>

<template>
	<table class="order-info-table">
		<tbody>
			<tr>
				<th>Phương thức thanh toán</th>
				<td>{{ order.payment.method }}</td>
			</tr>

			<tr>
				<th>Giá tiền các mặt hàng sơ bộ</th>
				<td>
					<b>
						{{ formatCurrencyVND(order.payment.subTotalPrice) }}
					</b>
				</td>
			</tr>

			<tr>
				<th>Giá tiền cuối cùng</th>
				<td>
					<b class="text-primary">
						{{ formatCurrencyVND(order.payment.totalPrice) }}
					</b>
				</td>
			</tr>
			<tr>
				<th>Số tiền đã trả</th>
				<td>
					<b class="text-success">{{
						formatCurrencyVND(order.payment.amountPaid)
					}}</b>
				</td>
			</tr>
			<tr>
				<th>Chi phí giao hàng</th>
				<td>
					{{ formatCurrencyVND(order.payment.shippingPrice) }}
				</td>
			</tr>

			<tr>
				<th>Trạng thái giao dịch</th>
				<td>{{ order.payment.status }}</td>
			</tr>

			<tr>
				<th>Ngày thanh toán</th>
				<td>
					{{ formatDate(order.payment.paymentDate) }}
				</td>
			</tr>

			<tr>
				<th>Ngày tạo</th>
				<td>
					{{ formatDate(order.payment.createdAt) }}
				</td>
			</tr>

			<tr>
				<th>Ngày sửa đổi</th>
				<td>
					{{ formatDate(order.payment.updatedAt) }}
				</td>
			</tr>
		</tbody>
	</table>
</template>

<style scoped></style>
