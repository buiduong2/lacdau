<script setup lang="ts">
import * as z from 'zod'
const registerSchema = z.object({
	email: z
		.string({ required_error: 'Mời bạn nhập địa chỉ Email' })
		.email({ message: 'Mời bạn nhập địa chỉ email đúng định dạng' }),
	name: z.string().optional(),
	phone: z
		.string({ required_error: 'Mời bạn nhập số điện thoại' })
		.regex(/^\+?\d+$/, 'Số điện thoại không được chữa chữ cái')
		.regex(/^\+?(84|0)(3|5|7|8|9)[0-9]{8}$/, 'Số điện thoại không hợp lệ'),
	gender: z.enum(['MALE', 'FEMALE']).optional(),
	dob: z
		.object(
			{
				day: z.number({ required_error: 'Ngày không được để trống' }),
				month: z.number({
					required_error: 'Tháng không được để trống'
				}),
				year: z.number({ required_error: 'Năm không được để trống' })
			},
			{ message: 'Mời bạn nhập ngày tháng năm sinh' }
		)
		.optional(),
	password: z.string({ message: 'Mời bạn nhập mật khẩu' }),
	confirmPassword: z.string({
		required_error: 'Mời bạn nhập lặp lại mật khẩu'
	}),
	address: z.string({ message: ' Mời bạn nhập địa chỉ' }),
	city: z.number({ required_error: 'Mời bạn chọn tỉnh thành phố' }).optional()
})

registerSchema.refine(data => data.password === data.confirmPassword, {
	message: 'Mật khẩu không trùng khớp vui lòng nhập lại',
	path: ['confirmPassword']
})
const { submit } = useForm(registerSchema)
const onSubmit = submit(value => {
	console.log(value)
})
</script>
<template>
	<AppBreadcrumb
		:breadcrumbs="[
			{
				href: 'register',
				id: 'random',
				name: 'Đăng ký tài khoản thành viên'
			}
		]"
	/>

	<section class="section-form">
		<div class="container">
			<div class="section-form-inner">
				<h3 class="section-title">Tạo tài khoản khách hàng cá nhân</h3>

				<form class="form" @submit.prevent="onSubmit">
					<div class="row items-center gx-4 gy-4">
						<AppFieldInput
							name="email"
							label="Email Đăng kí"
							type="email"
							required
						/>
						<AppFieldInput name="name" label="Tên" type="text" />

						<AppFieldInput
							name="phone"
							label="Số điện thoại"
							type="text"
							required
						/>

						<AppFieldRadio
							label="Giới tính"
							name="gender"
							:values="[
								{ label: 'Nam', value: 'MALE' },
								{ label: 'Nữ', value: 'FEMALE' }
							]"
						/>

						<AppFieldSelectDob />

						<AppFieldInput
							name="password"
							label="Mật Khẩu"
							type="password"
							required
						/>

						<AppFieldInput
							type="password"
							label="Nhập lại mật khẩu"
							name="confirmPassword"
							required
						/>

						<AppFieldInput
							type="text"
							label="Địa chỉ"
							name="address"
							required
						/>

						<AppFieldSelectCity />

						<div class="col-3"></div>
						<div class="col-9">
							<div class="form-action">
								<button class="form-btn">ĐĂNG KÝ</button>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</section>
</template>

<style lang="scss">
@use '@/assets/scss/pages/register.scss';
</style>
