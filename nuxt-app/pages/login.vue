<script setup lang="ts">
import * as z from 'zod'

const loginShema = z.object({
	email: z
		.string({ required_error: 'Mời bạn nhập địa chỉ Email' })
		.email({ message: 'Mời bạn nhập địa chỉ email đúng định dạng' }),
	password: z.string({ message: 'Mời bạn nhập mật khẩu' })
})

const form = useForm(loginShema)

const handleLogin = form.submit(value => {})
</script>
<template>
	<AppBreadcrumb
		:breadcrumbs="[
			{ href: 'login', id: 'random', name: 'Đăng nhập tài khoản' }
		]"
	/>
	<section class="section-login">
		<div class="container">
			<div class="row">
				<div class="col-6">
					<form
						class="form"
						method="POST"
						@submit.prevent="handleLogin"
					>
						<h3 class="section-title">
							Thông tin khách hàng đăng nhập hệ thống
						</h3>

						<div class="row content-center items-center gx-3 gy-3">
							<AppFieldInput
								name="email"
								type="email"
								label="Email đăng nhập"
							/>

							<AppFieldInput
								name="password"
								type="password"
								label="Mật Khẩu"
							/>

							<div class="col-3"></div>
							<div class="col-9">
								<div class="form-action">
									<button class="form-btn">Đăng nhập</button>
									<NuxtLink
										class="form-link"
										to="/password-recovery"
										>Quên mật khẩu</NuxtLink
									>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="col-6">
					<div class="section-detail">
						<h3 class="section-title">Bạn chưa là thành viên ?</h3>
						<p class="section-para">
							Đăng ký là thành viên để hưởng nhiều lợi ích và đặt
							mua hàng dễ dàng hơn.
						</p>

						<div class="section-action">
							<NuxtLink class="section-link" to="/register"
								>Đăng ký tài khoản</NuxtLink
							>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>
</template>

<style scoped lang="scss">
@use '@/assets/scss/pages/login.scss';
</style>
