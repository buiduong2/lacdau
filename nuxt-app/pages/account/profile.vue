<script setup lang="ts">
import { z } from 'zod'

const schema = z.object({
	name: z.string(),
	email: z.string().email(),
	address: z.string(),
	city: z.string(),
	phone: z.string()
})

const form = useForm(schema)
</script>
<template>
	<article class="section-view view--profile">
		<h3 class="view-title">Cập nhật thông tin cá nhân</h3>
		<form method="POST" class="form">
			<div class="row content-center items-center gx-3 gy-3">
				<AppFieldInput
					label="Họ tên"
					name="name"
					type="text"
					col-label="col-2"
					col-input="col-10"
				/>
				<AppFieldInput
					label="Email"
					name="email"
					type="email"
					col-label="col-2"
					col-input="col-10"
				/>
				<AppFieldSelectCity col-label="col-2" col-input="col-10" />
				<AppFieldInput
					label="Điện thoại di động"
					name="phone"
					type="text"
					col-label="col-2"
					col-input="col-10"
				/>

				<div class="col-2"></div>
				<div class="col-10">
					<div class="form-action">
						<button class="form-btn">THAY ĐỔI</button>
					</div>
				</div>
			</div>
		</form>
	</article>
</template>

<style scoped></style>
