<template>
	<article class="section-view">
		<h3 class="view-title">Thay đổi mật khẩu</h3>
		<form method="POST" class="form">
			<div class="row content-center items-center gx-3 gy-3">
				<AppFieldInput
					type="password"
					name="password"
					col-label="col-2"
					col-input="col-10"
					label="Mật Khẩu cũ"
					placeholder="Nhập mật khẩu cũ"
				/>

				<AppFieldInput
					type="password"
					name="newPassword"
					col-label="col-2"
					col-input="col-10"
					label="Mật Khẩu mới"
					placeholder="Nhập mật khẩu mới"
				/>

				<AppFieldInput
					type="password"
					name="confirmNewPassword"
					col-label="col-2"
					col-input="col-10"
					label="Nhập lại"
					placeholder="Nhập lại mật khẩu mới"
				/>
				<div class="col-2"></div>
				<div class="col-10">
					<div class="form-action">
						<button class="form-btn">CẬP NHẬT</button>
					</div>
				</div>
			</div>
		</form>
	</article>
</template>

<script setup lang="ts">
import { z } from 'zod'

const schema = z.object({
	password: z.string(),
	newPassword: z.string(),
	confirmNewPassword: z.string()
})

const form = useForm(schema)
</script>

<style scoped></style>
