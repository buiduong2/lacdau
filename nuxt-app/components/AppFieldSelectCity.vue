<template>
	<div :class="colLabel">
		<label class="form-label" for="city">Tỉnh/TP</label>
	</div>
	<div :class="colInput">
		<select class="form-select" name="city" @change="onSelect">
			<option :value="index" v-for="(city, index) in citys" :key="city">
				{{ city }}
			</option>
		</select>
		<p class="form-message error" v-if="error">{{ error }}</p>
	</div>
</template>

<script setup lang="ts">
withDefaults(defineProps<{ colLabel?: string; colInput?: string }>(), {
	colInput: 'col-9',
	colLabel: 'col-3'
})
const citys = [
	' Chọn tỉnh/Thành phố ',
	' Hà Nội ',
	' TP HCM ',
	' Hải Phòng ',
	' Đà Nẵng ',
	' An Giang ',
	' Bà Rịa-Vũng Tàu ',
	' Bình Dương ',
	' Bình Phước ',
	' Bình Thuận ',
	' Bình Định ',
	' Bạc Liêu ',
	' Bắc Giang ',
	' Bắc Kạn ',
	' Bắc Ninh ',
	' Bến Tre ',
	' Cao Bằng ',
	' Cà Mau ',
	' Cần Thơ ',
	' Gia Lai ',
	' Hà Giang ',
	' Hà Nam ',
	' Hà Tĩnh ',
	' Hòa Bình ',
	' Hải Dương ',
	' Hậu Giang ',
	' Hưng Yên ',
	' Khánh Hòa ',
	' Kiên Giang ',
	' Kon Tum ',
	' Lai Châu ',
	' Lào Cai ',
	' Lâm Đồng ',
	' Lạng Sơn ',
	' Long An ',
	' Nam Định ',
	' Nghệ An ',
	' Ninh Bình ',
	' Ninh Thuận ',
	' Phú Thọ ',
	' Phú Yên ',
	' Quảng Bình ',
	' Quảng Nam ',
	' Quảng Ngãi ',
	' Quảng Ninh ',
	' Quảng Trị ',
	' Sóc Trăng ',
	' Sơn La ',
	' Tây Ninh ',
	' Thanh Hóa ',
	' Thái Bình ',
	' Thái Nguyên ',
	' Thừa Thiên-Huế ',
	' Tiền Giang ',
	' Trà Vinh ',
	' Tuyên Quang ',
	' Vĩnh Long ',
	' Vĩnh Phúc ',
	' Yên Bái ',
	' Đắk Lắk ',
	' Đồng Nai ',
	' Đồng Tháp ',
	' Điện Biên ',
	' Đăk Nông '
]

const { error, setValue } = useField('city')

function onSelect(e: Event) {
	const target = e.target as HTMLSelectElement
	const newVal: number | undefined =
		Number(target.value) <= 0 ? undefined : Number(target.value)

	setValue(newVal)
}
</script>

<style scoped></style>
