<script setup lang="ts">
import { Icon } from '@iconify/vue/dist/iconify.js'

const breadcrumbs: Breadcrumb[] = [
	{
		id: '123',
		name: 'Thông tin giỏ hàng',
		href: '/cart'
	}
]
</script>
<template>
	<AppBreadcrumb :breadcrumbs="breadcrumbs" />

	<section class="container section-cart">
		<div class="cart-header">
			<div class="header-row header-row--action">
				<h2 class="header-title">THÔNG TIN GIỎ HÀNG</h2>
				<div class="header-action__list">
					<NuxtLink class="header-action__item">
						<Icon icon="fa6-solid:left-long" />
						CHỌN TIẾP SẢN PHẨM KHÁC
					</NuxtLink>
					<button class="header-action__item">XÓA GIỎ HÀNG</button>
				</div>
			</div>
			<div class="header-row header-row--title">
				<div class="cart-col__img">Sản phẩm</div>
				<div class="cart-col__price">Đơn giá</div>
				<div class="cart-col__quantity">Số lượng</div>
				<div class="cart-col__price">Số tiền</div>
				<div class="cart-col__action">Thao tác</div>
			</div>
		</div>

		<div class="cart-body cart-list">
			<div class="cart-item cart-row">
				<div class="cart-col__img cart-item__img">
					<img src="/assets/imgs/product-1.jpg" />
				</div>

				<div class="cart-col__info">
					<h4 class="cart-item__title">
						<NuxtLink to="product/1"
							>CHUỘT GAMING E-DRA EM6102 RGB TRẮNG</NuxtLink
						>
					</h4>
					<p class="cart-item__id">
						<Icon icon="fa6-solid:circle" /> Mã sản phẩm: CHV0003
					</p>
				</div>
				<div class="cart-col__price">
					<p class="cart-item__price">145.000 đ</p>
				</div>
				<div class="cart-col__quantity">
					<div class="cart-item__quantity">
						<button class="quantity__btn" disabled>
							<Icon icon="fa6-solid:minus" />
						</button>
						<input
							type="number"
							value="100"
							step="1"
							pattern="\d+"
						/>
						<button class="quantity__btn">
							<Icon icon="fa6-solid:plus" />
						</button>
					</div>
				</div>
				<div class="cart-col__price">
					<p class="cart-item__total-price">145.000 đ</p>
				</div>
				<div class="cart-col__action">
					<div class="cart-item__action">
						<Icon icon="fa6-regular:trash-can" />
						Xóa
					</div>
				</div>
			</div>

			<div class="cart-item cart-row">
				<div class="cart-col__img cart-item__img">
					<img src="/assets/imgs/product-1.jpg" />
				</div>

				<div class="cart-col__info">
					<h4 class="cart-item__title">
						<NuxtLink to="product/1"
							>CHUỘT GAMING E-DRA EM6102 RGB TRẮNG</NuxtLink
						>
					</h4>
					<p class="cart-item__id">
						<Icon icon="fa6-solid:circle" /> Mã sản phẩm: CHV0003
					</p>
				</div>
				<div class="cart-col__price">
					<p class="cart-item__price">145.000 đ</p>
				</div>
				<div class="cart-col__quantity">
					<div class="cart-item__quantity">
						<button class="quantity__btn" disabled>
							<Icon icon="fa6-solid:minus" />
						</button>
						<input
							type="number"
							value="100"
							step="1"
							pattern="\d+"
						/>
						<button class="quantity__btn">
							<Icon icon="fa6-solid:plus" />
						</button>
					</div>
				</div>
				<div class="cart-col__price">
					<p class="cart-item__total-price">145.000 đ</p>
				</div>
				<div class="cart-col__action">
					<div class="cart-item__action">
						<Icon icon="fa6-regular:trash-can" />
						Xóa
					</div>
				</div>
			</div>
		</div>

		<div class="cart-footer">
			<div class="row gx-4">
				<div class="col-8">
					<h3 class="cart-footer__title">
						<Icon icon="fa6-solid:map-location-dot" />
						ĐỊA CHỈ NHẬN HÀNG
					</h3>
					<div class="row gx-3 gy-0">
						<div class="col-6">
							<div class="cart-footer__item item--personal-info">
								<input
									type="text"
									placeholder="Họ và tên"
									class="grid-col-1-3"
								/>
								<input
									type="email"
									placeholder="Email"
									class="gird-col-1-2"
								/>
								<input
									type="text"
									placeholder="Số điện thoại"
									class="grid-col-2-3"
								/>
								<select class="grid-col-1-3">
									<option value="#">Hà Nội</option>
									<option value="#">Hải Phòng</option>
								</select>
								<select class="grid-col-1-3">
									<option value="#">Quận/Huyện</option>
								</select>
								<input
									class="grid-col-1-3"
									type="text"
									placeholder="Tòa nhà, Tên đường ..."
								/>
							</div>
						</div>
						<div class="col-6">
							<div class="cart-footer__item item--message">
								<textarea
									placeholder="Nhập ghi chú cho chúng tôi"
									class="grid-row-1-4 grid-col-1-3"
								></textarea>
								<button class="grid-col-1-2">
									TẢI ẢNH ĐƠN HÀNG
								</button>
								<button class="grid-col-2-3">
									TẢI FILE EXCEL
								</button>
								<button class="grid-col-1-3">IN BÁO GIÁ</button>
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
									<label for="cod">
										<input
											type="radio"
											name="paymment-method"
											id="cod"
											checked
										/>
										<span
											>Thanh toán trước qua chuyển
											khoản</span
										>
									</label>
									<label for="visa">
										<input
											type="radio"
											name="paymment-method"
											id="visa"
										/>
										<span
											>Thanh toán trước qua chuyển
											khoản</span
										>
									</label>
								</div>
								<div class="payment-voucher">
									<div class="payment-voucher-inner">
										<input
											class="payment-voucher__input"
											type="text"
											placeholder="Mã voucher"
										/>
										<button class="payment-voucher__btn">
											<Icon class="payment-voucher__icon" icon="fa6-solid:ticket" />Nhập
											mã voucher
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
										>3 sản phẩm </span
									>):
									<strong class="payment-checkout__price"
										>80.000đ</strong
									>
								</p>

								<button class="payment-checkout__action">
									ĐẶT MUA NGAY
								</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>
</template>

<style scoped lang="scss">
@use '@/assets/scss/pages/cart.scss';
</style>
