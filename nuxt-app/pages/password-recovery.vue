<script setup lang="ts">
import * as z from 'zod'

const recoverySchema = z.object({
	email: z
		.string({ required_error: 'Mời bạn nhập địa chỉ Email' })
		.email({ message: 'Mời bạn nhập địa chỉ email đúng định dạng' })
})

const form = useForm(recoverySchema)
const submit = form.submit(value => {})
</script>
<template>
	<AppBreadcrumb
		:breadcrumbs="[
			{
				href: 'password-recovery',
				name: 'LẤY LẠI MẬT KHẨU',
				id: 'random'
			}
		]"
	/>
	<section class="section-password-recovery">
		<div class="container">
			<h2 class="section-title">Bạn quên mật khẩu vào tài khoản?</h2>
			<p class="section-desc">
				Vui lòng nhập địa chỉ email đã đăng ký với chúng tôi để tạo mật
				khẩu mới. Chúng tôi sẽ gửi 1 email vào địa chỉ email cung cấp và
				yêu cầu xác minh trước khi có thể tạo mật khẩu mới
			</p>

			<form method="post" class="form" @submit.prevent="submit">
				<div class="row">
					<AppFieldInput
						type="email"
						label="Nhập Email Đăng kí"
						name="email"
					/>
					<div class="col-3"></div>
					<div class="col-9 form-action">
						<button class="form-btn">LẤY MẬT KHẨU</button>
					</div>
				</div>
			</form>
		</div>
	</section>
</template>

<style scoped lang="scss">
@use '@/assets/scss/pages/password-recovery.scss';
</style>
