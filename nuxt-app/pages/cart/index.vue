<script setup lang="ts">
const breadcrumbs: Breadcrumb[] = [
	{
		id: '123',
		name: 'Thông tin giỏ hàng',
		href: 'cart'
	}
]

const cartStore = useCartStore()
const router = useRouter()
const { notification } = useNotification()
async function submitCart(payload: Omit<OrderCreateReq, 'orderItems'>) {
	const orderItems: OrderCreateReq['orderItems'] = [...cartStore.items]
	const body: OrderCreateReq = {
		orderItems,
		...payload
	}
	const res = await $fetch('/api/orders', { method: 'POST', body })
	notification('Đặt đơn hàng thành công')
	cartStore.clearCart()
}

onMounted(async () => {
	await cartStore.initializeCart()
})
</script>
<template>
	<AppBreadcrumb :breadcrumbs="breadcrumbs" />

	<section class="container section-cart" v-if="!cartStore.isCartEmpty">
		<CartOrderList
			@clear-cart="cartStore.clearCart"
			:cart-items="cartStore.items"
			@remove-from-cart="item => cartStore.removeFromCart(item.productId)"
			@update-cart-item="
				payload =>
					cartStore.updateCartItem(
						payload.item.productId,
						payload.quantity
					)
			"
		/>
		<CartCheckout
			:total-item="cartStore.itemCount"
			:total-price="cartStore.subTotalPrice"
			@submit-cart="submitCart"
		/>
	</section>
	<section class="container section-cart" v-else>
		<p class="cart-empty-msg">Có 0 sản phẩm trong giỏ hàng</p>
	</section>
</template>

<style lang="scss">
@use '@/assets/scss/pages/cart.scss';
</style>
