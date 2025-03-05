<script setup lang="ts">
const { data, error } = await useFetch('/api/customers', {
	method: 'GET'
})
const { notification } = useNotification()

if (error.value || !data.value) {
	throw createError({
		message: 'Chưa có thông tin khách hàng',
		status: 500
	})
}

async function editCustomerProfile(payload: {
	data: CustomerRegisterReq
	done: Function
}) {
	try {
		await $fetch('/api/customers', { method: 'put', body: payload.data })
		notification('Cập nhật thông tin thành công')
	} catch (error) {
		notification('Cập nhật thông tin không thành công. Đã có lỗi xảy ra', {
			isSuccess: false
		})
	} finally {
		payload.done()
	}
}
</script>
<template>
	<article class="section-view view--profile" v-if="data">
		<h3 class="view-title">Cập nhật thông tin cá nhân</h3>
		<AccountCustomerForm
			:default-values="data"
			@submit="editCustomerProfile"
		/>
	</article>
</template>

<style scoped></style>
