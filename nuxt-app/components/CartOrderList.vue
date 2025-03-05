<script setup lang="ts">
import { Icon } from '@iconify/vue/dist/iconify.js'
defineProps<{ cartItems: CartItem[] }>()
const emit = defineEmits<{
	(e: 'clearCart'): void
	(e: 'removeFromCart', payload: CartItem): void
	(e: 'updateCartItem', payload: { item: CartItem; quantity: number }): void
}>()

function handleInputQuantityChange(e: Event, item: CartItem) {
	const target = e.target as HTMLInputElement
	const numberStr = target.value
	const orderedQuantity = Math.floor(Number(numberStr))
	if (numberStr === '') {
		return
	} else if (isNaN(orderedQuantity)) {
		target.value = String(item.orderedQuantity)
	} else if (orderedQuantity <= 0) {
		target.value = '1'
		emit('updateCartItem', { item, quantity: 1 })
	} else if (orderedQuantity > item.quantity) {
		target.value = String(item.quantity)
		emit('updateCartItem', { item, quantity: item.quantity })
	} else {
		target.value = String(orderedQuantity)
		emit('updateCartItem', { item, quantity: orderedQuantity })
	}
}

function handleInputBlur(e: Event, item: CartItem) {
	const target = e.target as HTMLInputElement
	const numberStr = target.value
	if (numberStr === '') {
		target.value = String(item.orderedQuantity)
	}
}
</script>
<template>
	<div class="cart-header">
		<div class="header-row header-row--action">
			<h2 class="header-title">THÔNG TIN GIỎ HÀNG</h2>
			<div class="header-action__list">
				<a class="header-action__item" @click.prevent="$router.back()">
					<Icon icon="fa6-solid:left-long" />
					CHỌN TIẾP SẢN PHẨM KHÁC
				</a>
				<button
					class="header-action__item"
					@click.prevent="$emit('clearCart')"
				>
					XÓA GIỎ HÀNG
				</button>
			</div>
		</div>
		<div class="header-row header-row--title">
			<div class="cart-col__img">Sản phẩm</div>
			<div class="cart-col__info"></div>
			<div class="cart-col__price">Đơn giá</div>
			<div class="cart-col__quantity">Số lượng</div>
			<div class="cart-col__price">Số tiền</div>
			<div class="cart-col__action">Thao tác</div>
		</div>
	</div>

	<div class="cart-body cart-list">
		<div
			class="cart-item cart-row"
			v-for="item in cartItems"
			:key="item.productId"
		>
			<div class="cart-col__img cart-item__img">
				<img
					v-if="item.mainImage"
					:src="item.mainImage.src"
					:alt="item.mainImage.alt"
				/>
				<img
					v-else
					src="/imgs/product-placeholder.png"
					:alt="item.name"
				/>
			</div>

			<div class="cart-col__info">
				<h4 class="cart-item__title">
					<NuxtLink :to="`/product/${item.productId}`">{{
						item.name
					}}</NuxtLink>
				</h4>
				<p class="cart-item__id">
					<Icon icon="fa6-solid:circle" /> Mã sản phẩm:
					{{ item.productId }}
				</p>
				<p class="cart-item__msg">
					<span v-if="item.orderedQuantity > item.quantity"
						>Sản phẩm tồn kho không đủ cung cấp</span
					>
				</p>
			</div>
			<div class="cart-col__price">
				<p class="cart-item__price">
					{{
						formatCurrencyVND(item.salePrice || item.originalPrice)
					}}
				</p>
			</div>
			<div class="cart-col__quantity">
				<div class="cart-item__quantity">
					<button
						class="quantity__btn"
						:disabled="item.orderedQuantity <= 1"
						@click.prevent="
							$emit('updateCartItem', {
								item,
								quantity: item.orderedQuantity - 1
							})
						"
					>
						<Icon icon="fa6-solid:minus" />
					</button>
					<input
						type="number"
						:value="item.orderedQuantity"
						step="1"
						@input="handleInputQuantityChange($event, item)"
						@blur="handleInputBlur($event, item)"
					/>
					<button
						class="quantity__btn"
						:disabled="item.orderedQuantity >= item.quantity"
						@click.prevent="
							$emit('updateCartItem', {
								item: item,
								quantity: item.orderedQuantity + 1
							})
						"
					>
						<Icon icon="fa6-solid:plus" />
					</button>
				</div>
			</div>
			<div class="cart-col__price">
				<p class="cart-item__total-price">
					{{ formatCurrencyVND(item.totalPrice) }}
				</p>
			</div>
			<div class="cart-col__action">
				<button
					class="cart-item__action"
					@click="$emit('removeFromCart', item)"
				>
					<Icon icon="fa6-regular:trash-can" /> Xóa
				</button>
			</div>
		</div>
	</div>
</template>

<style scoped></style>
