<script setup lang="ts">
import { Icon } from '@iconify/vue/dist/iconify.js'
import z from 'zod'
defineProps<{ totalPrice: number; totalItem: number }>()
const emit = defineEmits<{
	(e: 'submitCart', payload: Omit<OrderCreateReq, 'orderItems'>): void
}>()

const actionSchema = z.object({
	fullName: z.string(),
	email: z.string().email(),
	phone: z.string().regex(/^(?:\+84|0)(3|5|7|8|9)\d{8}$/, {
		message: 'SĐT không hợp lệ'
	}),
	provinceId: z.number(),
	districtId: z.number(),
	detail: z.string(),
	message: z.string().optional(),
	paymentMeThod: z.enum(['COD', 'CARD', 'CASH'], {
		message: 'Hãy chọn phương thức thanh toán'
	})
})

const { submit } = useForm(actionSchema)
const { provinces, districts, districtId, provinceId } = await useProvinces()

function submitCart() {

	submit(async value => {
		console.log('submit')
		emit('submitCart', {
			address: {
				detail: value.detail,
				districtId: value.districtId,
				email: value.email,
				fullName: value.fullName,
				phone: value.phone,
				message: value.message
			},
			paymentMethod: value.paymentMeThod
		})
	})
}

function downloadOrderImage() {
	alert('Chức năng chưa hoạt động 1')
}
function downloadExcelFile() {
	alert('Chức năng chưa hoạt động 2')
}
function printQuotation() {
	alert('Chức năng chưa hoạt động 3')
}

function submitVoucherCode() {
	alert('Chức năng chưa hoạt động 4')
}
</script>

<template>
	<form class="cart-footer" method="POST" @submit.prevent="submitCart">
		<div class="row gx-4">
			<div class="col-8">
				<h3 class="cart-footer__title">
					<Icon icon="fa6-solid:map-location-dot" />
					ĐỊA CHỈ NHẬN HÀNG
				</h3>
				<div class="row gx-3 gy-0">
					<div class="col-6">
						<div class="cart-footer__item item--personal-info">
							<div class="grid-col-1-3 cart-field">
								<CardCheckoutInput
									name="fullName"
									placeholder="Họ và tên"
									type="text"
								/>
							</div>
							<div class="grid-col-1-2 cart-field">
								<CardCheckoutInput
									name="email"
									placeholder="Email"
									type="email"
								/>
							</div>
							<div class="grid-col-2-3 cart-field">
								<CardCheckoutInput
									name="phone"
									placeholder="Số điện thoại"
									type="text"
								/>
							</div>
							<div class="grid-col-1-3 cart-field">
								<CartCheckoutSelect
									name="provinceId"
									v-model="provinceId"
								>
									<option :value="undefined">
										Quận/Huyện
									</option>
									<option
										v-for="province in provinces"
										:key="province.id"
										:value="province.id"
									>
										{{ province.name }}
									</option>
								</CartCheckoutSelect>
							</div>
							<div class="grid-col-1-3 cart-field">
								<CartCheckoutSelect
									name="districtId"
									v-model="districtId"
								>
									<option :value="undefined">
										Phường/Xã
									</option>
									<option
										v-for="district in districts"
										:key="district.id"
										:value="district.id"
									>
										{{ district.name }}
									</option>
								</CartCheckoutSelect>
							</div>
							<div class="grid-col-1-3 cart-field">
								<CardCheckoutInput
									name="detail"
									placeholder="Tòa nhà, Tên đường ..."
									type="text"
								/>
							</div>
						</div>
					</div>
					<div class="col-6">
						<div class="cart-footer__item item--message">
							<div class="grid-row-1-4 grid-col-1-3">
								<CartCheckoutTextarea
									name="message"
									placeholder="Nhập ghi chú cho chúng tôi"
								/>
							</div>
							<button
								class="grid-col-1-2"
								type="button"
								@click="downloadOrderImage"
							>
								TẢI ẢNH ĐƠN HÀNG
							</button>
							<button
								class="grid-col-2-3"
								type="button"
								@click="downloadExcelFile"
							>
								TẢI FILE EXCEL
							</button>
							<button
								class="grid-col-1-3"
								type="button"
								@click="printQuotation"
							>
								IN BÁO GIÁ
							</button>
						</div>
					</div>
				</div>
			</div>
			<div class="col-4">
				<h3 class="cart-footer__title">
					<Icon icon="fa6-solid:credit-card" />
					HÌNH THỨC THANH TOÁN
				</h3>
				<div class="cart-footer__item item--payment">
					<div class="grid-row-1-4 grid-col-1-3">
						<div class="payment-body">
							<div class="payment-method">
								<CartCheckoutFieldRadio />
							</div>
							<div class="payment-voucher">
								<div class="payment-voucher-inner">
									<input
										class="payment-voucher__input"
										type="text"
										placeholder="Mã voucher"
									/>
									<button
										class="payment-voucher__btn"
										@click="submitVoucherCode"
										type="button"
									>
										<Icon
											class="payment-voucher__icon"
											icon="fa6-solid:ticket"
										/>Nhập mã voucher
									</button>
								</div>
							</div>
						</div>
					</div>

					<div class="grid-col-1-3 grid-row-4-6">
						<div class="payment-checkout">
							<p class="payment-checkout__info">
								Tổng tiền hàng (<span
									class="payment-checkout__quantity"
									>{{ totalItem }} sản phẩm </span
								>):
								<strong class="payment-checkout__price">{{
									formatCurrencyVND(totalPrice)
								}}</strong>
							</p>

							<button class="payment-checkout__action">
								ĐẶT MUA NGAY
							</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</form>
</template>
