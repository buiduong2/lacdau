<script setup lang="ts">
defineProps<{ order: OrderDetailRes }>()
</script>
<template>
	<table class="order-info-table">
		<tbody>
			<tr>
				<th>Tên</th>
				<td>{{ order.orderAddress.fullName }}</td>
			</tr>
			<tr>
				<th>Số điện thoại</th>
				<td>{{ order.orderAddress.phone }}</td>
			</tr>
			<tr>
				<th>Tỉnh/Thành Phố</th>
				<td>{{ order.orderAddress.province }}</td>
			</tr>
			<tr>
				<th>Quận/Huyện</th>
				<td>{{ order.orderAddress.district }}</td>
			</tr>
			<tr>
				<th>Chi tiết</th>
				<td>{{ order.orderAddress.detail }}</td>
			</tr>

			<tr>
				<th>Lời nhắn</th>
				<td>{{ order.orderAddress.message }}</td>
			</tr>
		</tbody>
	</table>
</template>

<style scoped></style>
