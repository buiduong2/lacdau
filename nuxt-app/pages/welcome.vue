<script setup lang="ts">
definePageMeta({
	layout: 'empty'
})

const showSuccessMessage = ref<boolean>(false)
const router = useRouter()
const authStore = useAuthStore()

const onSubmit = function (payload: {
	data: CustomerRegisterReq
	done: () => Promise<void>
}) {
	try {
		$fetch('/api/customers/', {
			method: 'POST',
			body: payload.data
		})
		showSuccessMessage.value = true

		setTimeout(() => {
			router.replace('/account')
		}, 3000)
	} catch (error: any) {
		if (error.status === 400) {
		} else if (error.status === 500) {
			throw createError({
				statusCode: 500,
				message: 'Có lỗi xảy ra trong việc đăng kí mới người dùng'
			})
		}
	} finally {
		payload.done()
	}
}
</script>

<template>
	<section class="section-form mt-5">
		<AppNotification
			msg="Cập nhật thông tin cá nhân thành công"
			:show="true"
			v-if="showSuccessMessage"
		/>
		<div class="container">
			<div class="section-form-inner">
				<button
					class="logout-btn"
					type="button"
					@click.prevent="authStore.logout()"
				>
					Thoát
				</button>
				<h3 class="section-title">
					Cập nhật thông tin để mua sắm dễ dàng hơn
				</h3>
				<p class="section-description">
					Chào mừng bện đến với <b class="website-name">Lắc Đầu </b>Để
					mang lại trải nghiệm mua sắm tốt nhất, vui lòng cung cấp một
					số thông tin cần thiết. Điều này giúp chúng tôi cá nhân hóa
					dịch vụ và đảm bảo đơn hàng được giao đúng địa chỉ.
				</p>

				<AccountCustomerForm @submit="onSubmit" />
			</div>
		</div>
	</section>
</template>

<style scoped lang="scss">
@use '@/assets/scss/pages/register.scss';
</style>
