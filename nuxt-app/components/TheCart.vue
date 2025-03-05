<script setup lang="ts">
defineProps<{
	items: CartItem[]
	itemCount: number
	subTotalPrice: number
	isCartEmpty: boolean
}>()
</script>
<template>
	<div class="cart">
		<p v-if="isCartEmpty" class="cart-empty-msg">
			Có 0 sản phẩm trong giỏ hàng
		</p>
		<div v-else class="cart-card">
			<div class="cart-body">
				<div class="cart-list">
					<div
						class="cart-item"
						v-for="cart in items"
						:key="cart.productId"
					>
						<div class="row">
							<div class="col-3">
								<img
									v-if="cart.mainImage"
									:src="cart.mainImage.src"
									:alt="cart.mainImage.alt"
									class="cart-item__img"
								/>
								<NuxtImg
									v-else
									src="/imgs/product-placeholder.png"
									:alt="cart.name"
									class="cart-item__img"
								/>
							</div>
							<div class="col-9">
								<div>
									<h3 class="cart-item__title">
										{{ cart.name }}
									</h3>
									<div class="cart-item__detail">
										<p class="cart-item__quantity">
											x{{ cart.orderedQuantity }}
										</p>
										<p class="cart-item__price">
											{{
												formatCurrencyVND(
													cart.totalPrice
												)
											}}
										</p>
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
					<span class="cart-total__quantity"
						>{{ itemCount }} Sản phẩm</span
					>
					):
					<span class="cart-total__price">
						{{ formatCurrencyVND(subTotalPrice) }}
					</span>
				</p>

				<div class="cart-action">
					<NuxtLink to="/cart" class="cart-btn">
						THANH TOÁN NGAY
					</NuxtLink>
				</div>
			</div>
		</div>
	</div>
</template>

<style scoped lang="scss">
@use '~/assets/scss/components/_header-cart.scss';
</style>
