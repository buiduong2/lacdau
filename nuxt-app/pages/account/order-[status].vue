<script setup lang="ts">
const route = useRoute('account-order-status')
const acceptStatuses = [
	'pending',
	'completed',
	'processing',
	'canceled'
] as const
type Status = (typeof acceptStatuses)[number]
const status = route.params.status as Status
if (!acceptStatuses.includes(status)) {
	throw createError({ status: 404, message: 'Không tìm thấy trang yêu cầu' })
}
const pageQuery = computed<number>(() => (Number(route.query.page) || 1) - 1)
const { data: orderPage } = useFetch('/api/orders', {
	query: { page: pageQuery, type: status }
})

const viewTitle = computed(() => {
	switch (status) {
		case 'canceled':
			return 'Danh sách đơn hàng đã hủy'
		case 'completed':
			return 'Danh sách đơn hàng đã hoàn thành'

		case 'pending':
			return 'Danh sách đơn hàng đang chờ xác nhận'
		default:
			return 'Danh sách đơn hàng đang giao'
	}
})

const viewMsg = computed(() => {
	switch (status) {
		case 'canceled':
			return 'Danh sách đơn hàng đã hủy'
		case 'completed':
			return 'Nếu bạn thấy hài lòng với sản phẩm hãy báo cho chúng tôi biết!'
		case 'pending':
			return 'Các nhân viên sẽ làm việc để hoàn tiền cho bạn nếu cần thiết'
		default:
			return 'Hãy chờ và theo dõi tiến trình đơn hàng của bạn'
	}
})
</script>

<template>
	<article class="section-view view--order" v-if="orderPage">
		<h2 class="view-title">{{ viewTitle }}</h2>
		<div v-if="!orderPage.content.length"><p>Không có đơn hàng nào</p></div>
		<AccountOrderTable v-else :orders="orderPage.content" />
		<CategoryPagination :total-page="orderPage.page.totalPages" />

		<hr />
		<p class="view-description">* {{ viewMsg }}</p>
	</article>
</template>
