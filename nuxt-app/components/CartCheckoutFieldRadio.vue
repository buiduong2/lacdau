<script setup lang="ts">
type PaymentMethod = 'COD' | 'CARD'

const model = defineModel<string>({ default: 'COD' })
const { setValue, value, error } = useField('paymentMeThod')

watch(
	[model],
	() => {
		setValue(model.value)
	},
	{ immediate: true }
)
</script>

<template>
	<label for="cod">
		<input
			type="radio"
			name="paymentMeThod"
			value="CARD"
			id="cod"
			v-model="model"
		/>
		<span>Thanh toán trước qua chuyển khoản</span>
	</label>
	<label for="visa">
		<input
			type="radio"
			name="paymentMeThod"
			value="COD"
			id="visa"
			v-model="model"
		/>
		<span>Thanh toán khi nhận hàng</span>
	</label>

	<p class="form-msg error">{{ error }}</p>
</template>

<style scoped></style>
