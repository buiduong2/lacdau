<script setup lang="ts">
const carts: any[] = [1, 2, 3, 4, 5]
</script>
<template>
	<div class="cart">
		<p v-if="!carts.length" class="cart-empty-msg">
			Có 0 sản phẩm trong giỏ hàng
		</p>
		<div v-else class="cart-card">
			<div class="cart-body">
				<div class="cart-list">
					<div class="cart-item" v-for="cart in carts" :key="cart">
						<div class="row">
							<div class="col-3">
								<img
									src="/public/imgs/product-1.jpg"
									alt=""
									class="cart-item__img"
								/>
							</div>
							<div class="col-9">
								<div>
									<h3 class="cart-item__title">
										PAD 83 YASUO AH LÃNG KIẾM
									</h3>
									<div class="cart-item__detail">
										<p class="cart-item__quantity">x1</p>
										<p class="cart-item__price">79.000 đ</p>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="cart-footer">
				<p class="cart-total">
					Tổng tiền hàng (
					<span class="cart-total__quantity">1 Sản phẩm</span>
					):
					<span class="cart-total__price"> 69.000đ </span>
				</p>

				<div class="cart-action">
					<NuxtLink to="/cart" class="cart-btn">THANH TOÁN NGAY</NuxtLink>
				</div>
			</div>
		</div>
	</div>
</template>

<style scoped lang="scss">
@use '~/assets/scss/components/_header-cart.scss';
</style>
