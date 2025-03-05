<script setup lang="ts">
import { z } from 'zod'
type DoneFn = () => Promise<void>
const emit = defineEmits<{
	(
		e: 'submit',
		payload: {
			data: CustomerRegisterReq
			done: DoneFn
		}
	): void
}>()

const props = defineProps<{ defaultValues?: DeepPartial<Customer> }>()
let registerSchema = z.object({
	email: z
		.string({ required_error: 'Mời bạn nhập địa chỉ Email' })
		.email({ message: 'Mời bạn nhập địa chỉ email đúng định dạng' }),
	firstName: z.string({ message: 'Không được để trống' }),
	lastName: z.string({ message: 'Không được để trống' }),
	phone: z
		.string({ required_error: 'Mời bạn nhập số điện thoại' })
		.regex(/^\+?\d+$/, 'Số điện thoại không được chữa chữ cái')
		.regex(
			/^\+?(84|0)(3|5|7|8|9)[0-9]{8}$/,
			'Số điện thoại không hợp lệ. /^\+?(84|0)(3|5|7|8|9)[0-9]{8}$/'
		),
	gender: z.enum(['MALE', 'FEMALE'], { message: 'Không được bỏ trống' }),
	dob: z.object(
		{
			day: z.number({ message: 'Ngày không được để trống' }),
			month: z.number({
				message: 'Tháng không được để trống'
			}),
			year: z.number({ message: 'Năm không được để trống' })
		},
		{ message: 'Thông tin không hợp lệ' }
	),
	addressDetail: z.string({ message: ' Mời bạn nhập địa chỉ' }),
	addressDistrictId: z.number({ required_error: 'Không được bỏ trống' }),
	addressProvinceId: z.number({ required_error: 'Không được bỏ trống' })
})

type DefaultValue = DeepPartial<z.infer<typeof registerSchema>>

let initializeValues: DefaultValue = {}

if (props.defaultValues) {
	initializeValues = {
		addressDetail: props.defaultValues?.address?.detail,
		addressDistrictId: props.defaultValues?.address?.districtId,
		addressProvinceId: props.defaultValues?.address?.provinceId,
		email: props.defaultValues?.email,
		firstName: props.defaultValues?.firstName,
		gender: props.defaultValues?.gender,
		lastName: props.defaultValues?.lastName,
		phone: props.defaultValues?.phone
	}
	if (props.defaultValues.dob) {
		const dob: DefaultValue['dob'] = {}

		const date = new Date(props.defaultValues.dob)
		dob.day = date.getDate()
		dob.month = date.getMonth() + 1
		dob.year = date.getFullYear()

		initializeValues.dob = dob
	}
}

const { submit } = useForm(registerSchema, { initialValues: initializeValues })
const { provinces, districts, districtId, provinceId } = await useProvinces({
	initialValues: {
		districtId: initializeValues.addressDistrictId,
		provinceId: initializeValues.addressProvinceId
	}
})

const onSubmit = function () {
	submit(async value => {
		const customerReq: CustomerRegisterReq = {
			address: {
				detail: value.addressDetail,
				districtId: value.addressDistrictId
			},
			dob: `${value.dob.year}-${String(value.dob.month).padStart(
				2,
				'0'
			)}-${String(value.dob.day).padStart(2, '0')}`,
			email: value.email,
			firstName: value.firstName,
			gender: value.gender,
			lastName: value.lastName,
			phone: value.phone
		}
		let doneFn: DoneFn

		let promise = new Promise<any>(res => {
			doneFn = async () => res(undefined)
		})

		emit('submit', { data: customerReq, done: doneFn! })
		await promise
	})
}
</script>

<template>
	<form class="form" @submit.prevent="onSubmit">
		<div class="row items-center gx-4 gy-4">
			<AppFieldInput name="email" label="Email" type="email" />
			<AppFieldInput name="firstName" label="Tên" type="text" />
			<AppFieldInput name="lastName" label="Họ" type="text" />

			<AppFieldInput name="phone" label="Số điện thoại" type="text" />

			<AppFieldRadio
				label="Giới tính"
				name="gender"
				:values="[
					{ label: 'Nam', value: 'MALE' },
					{ label: 'Nữ', value: 'FEMALE' }
				]"
			/>

			<AppFieldSelectDob />

			<AppFieldInput type="text" label="Địa chỉ" name="addressDetail" />
			<AppFieldSelect
				label="Tỉnh/Thành phố"
				name="addressProvinceId"
				v-model="provinceId"
			>
				<option :value="undefined">Tỉnh/Thành Phố</option>
				<option
					v-for="province in provinces"
					:key="province.id"
					:value="province.id"
				>
					{{ province.name }}
				</option>
			</AppFieldSelect>

			<AppFieldSelect
				label="Quận/Huyện"
				name="addressDistrictId"
				v-model="districtId"
			>
				<option :value="undefined">Quận/Huyện</option>
				<option
					v-for="district in districts"
					:key="district.id"
					:value="district.id"
				>
					{{ district.name }}
				</option>
			</AppFieldSelect>

			<div class="col-3"></div>
			<div class="col-9">
				<div class="form-action">
					<button class="form-btn">Cập nhật</button>
				</div>
			</div>
		</div>
	</form>
</template>

<style scoped></style>
