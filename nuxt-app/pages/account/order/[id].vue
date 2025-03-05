<script setup lang="ts">
import { Icon } from '@iconify/vue/dist/iconify.js'

const route = useRoute('account-order-id')
const orderId = route.params.id
const detailRes = useFetch<OrderDetailRes>(`/api/orders/${orderId}`)
const logRes = useFetch(`/api/orders/${orderId}/log`)

const [{ data: order, error }, { data: orderLogs, error: errorLog }] =
	await Promise.all([detailRes, logRes])
if (error.value || errorLog.value) {
	throw createError({
		status: error.value?.status || errorLog.value?.status,
		message: 'Có lỗi xảy ra khi tải thông tin'
	})
}
</script>
<template>
	<article class="section-view view--order-detail" v-if="order && orderLogs">
		<h3 class="view-title">
			Chi tiết đơn hàng: <b>#{{ order.id }}</b>
		</h3>
		<div class="row gx-4 gy-4">
			<div class="col-12">
				<div class="order-card">
					<div class="order-card-header">
						<h3 class="order-card-header__title">
							<Icon icon="lucide:package" /> Thông tin Đơn hàng:
						</h3>
					</div>
					<div class="order-card-body">
						<AccountOrderDetailOrderItemTable :order="order"/>
					</div>
				</div>
			</div>
			<div class="col-6">
				<div class="order-card">
					<div class="order-card-header">
						<h3 class="order-card-header__title">
							<Icon icon="lucide:credit-card" />
							Thông tin thanh toán:
						</h3>
					</div>
					<div class="order-card-body">
						<AccountOrderDetailPaymentTable :order="order" class="border-spacing-x-4" />
					</div>
				</div>
			</div>
			<div class="col-6">
				<div class="order-card">
					<div class="order-card-header">
						<h3 class="order-card-header__title">
							<Icon icon="lucide:clipboard-list" /> Thông tin
							trạng thái:
						</h3>
					</div>
					<div class="order-card-body">
						<table class="order-info-table border-spacing-x-4">
							<tbody>
								<tr>
									<th>Thanh toán</th>
									<td>{{ order.payment.status }}</td>
								</tr>
								<tr>
									<th>Giai đoạn</th>
									<td>{{ order.stage }}</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="col-6">
				<div class="order-card">
					<div class="order-card-header">
						<h3 class="order-card-header__title">
							<Icon icon="lucide:user" /> Thông tin người nhận:
						</h3>
					</div>
					<div class="order-card-body">
						<AccountOrderDetailAddressTable  :order="order" class="border-spacing-x-4"/>
					</div>
				</div>
			</div>
			<div class="col-6">
				<div class="order-card">
					<div class="order-card-header">
						<h3 class="order-card-header__title">
							<Icon icon="lucide:truck" />Thông tin giao hàng:
						</h3>
					</div>
					<div class="order-card-body">
						<AccountOrderDetailShipmentTable :order="order" class="border-spacing-x-4"/>
					</div>
				</div>
			</div>
			<div class="col-12">
				<div class="order-card">
					<div class="order-card-header">
						<h3 class="order-card-header__title">
							<Icon icon="lucide:chart-network" />
							Tiến trình:
						</h3>
					</div>
					<div class="order-card-body">
						<AccountOrderDetailProgressTable :order-logs="orderLogs" :order="order" class="border-spacing-x-4"/>
					</div>
				</div>
			</div>
		</div>
	</article>
</template>

<style lang="scss">
@use '@/assets/scss/pages/order-detail.scss';
</style>
