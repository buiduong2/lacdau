import { defineStore } from 'pinia'
import { z } from 'zod'

interface RawCartItem {
	productId: string
	orderedQuantity: number
}

const storedCartSchema = z.array(
	z.object({
		productId: z.string(),
		orderedQuantity: z.number().min(1).int()
	})
)

export const useCartStore = defineStore('cart', () => {
	const STORE_KEY = 'cart'

	const cart = ref<Map<string, CartItem>>(new Map())
	const isCartEmpty = computed<boolean>(() => !Boolean(cart.value.size))
	const items = computed<CartItem[]>(() => [...cart.value.values()])
	const itemCount = computed<number>(() =>
		items.value.reduce((total, curr) => total + curr.orderedQuantity, 0)
	)
	const subTotalPrice = computed<number>(() =>
		items.value.reduce((prev, curr) => prev + curr.totalPrice, 0)
	)
	const errors = ref<(string | undefined)[]>([])

	let isStoreLoaded = false

	function addToCart(product: ProductSummaryRes | ProductDetailRes): boolean {
		if (!canAddToCart(product)) {
			return false
		}

		if (cart.value.has(product.id)) {
			const item: CartItem = cart.value.get(product.id)!
			item.orderedQuantity++
			item.totalPrice = computedTotalPrice(product, item.orderedQuantity)
		} else {
			const newItem: CartItem = {
				totalPrice: computedTotalPrice(product, 1),
				mainImage:
					'mainImage' in product
						? product.mainImage
						: product.thumbnails?.[0] || null,
				name: product.name,
				orderedQuantity: 1,
				originalPrice: product.originalPrice,
				salePrice: product.salePrice,
				productId: product.id,
				quantity: product.quantity
			}
			cart.value.set(newItem.productId, newItem)
		}
		product.orderedQuantity = (product.orderedQuantity || 0) + 1

		return true
	}

	function canAddToCart(
		product: ProductSummaryRes | ProductDetailRes
	): boolean {
		if (product.quantity <= 0) {
			return false
		}
		const orderedQuantity = cart.value.get(product.id)?.orderedQuantity || 0
		const remainQuantity = product.quantity - orderedQuantity

		return remainQuantity > 0
	}

	function removeFromCart(productId: string) {
		cart.value.delete(productId)
	}

	function updateCartItem(productId: string, quantity: number) {
		if (cart.value.has(productId)) {
			const item = cart.value.get(productId)!
			item.orderedQuantity = quantity
			item.totalPrice = computedTotalPrice(item, item.orderedQuantity)
		}
	}

	function clearCart() {
		cart.value = new Map()
	}

	function saveCartToLocalStorage() {
		const storedCart: RawCartItem[] = items.value.map(item => ({
			productId: item.productId,
			orderedQuantity: item.orderedQuantity
		}))
		localStorage.setItem(STORE_KEY, JSON.stringify(storedCart))
	}

	async function loadCartFromLocalStorage(): Promise<void> {
		if (isStoreLoaded) return

		const storedCart: RawCartItem[] = getStoredCartFromLocalStorage()
		if (storedCart.length) {
			const products = await fetchProductDetails(
				storedCart.map(i => i.productId)
			)
			products.forEach(p => {
				const orderedQuantity = storedCart.find(
					c => c.productId === p.id
				)!.orderedQuantity
				const item: CartItem = {
					totalPrice: computedTotalPrice(p, orderedQuantity),
					name: p.name,
					mainImage: p.mainImage,
					orderedQuantity: orderedQuantity,
					originalPrice: p.originalPrice,
					productId: p.id,
					quantity: p.quantity,
					salePrice: p.salePrice
				}
				cart.value.set(item.productId, item)
			})
		}
		isStoreLoaded = true
	}

	function getStoredCartFromLocalStorage(): RawCartItem[] {
		const rawStoredCart = localStorage.getItem(STORE_KEY)
		if (!rawStoredCart) return []
		try {
			const parsedStoredCart = JSON.parse(rawStoredCart)
			return storedCartSchema.parse(parsedStoredCart)
		} catch (error) {
			localStorage.removeItem(STORE_KEY)
			return []
		}
	}

	async function fetchProductDetails(
		productId: string[]
	): Promise<ProductSummaryRes[]> {
		return $fetch('/api/products/list', { query: { ids: productId } })
	}

	async function initializeCart() {
		if (window) {
			await loadCartFromLocalStorage()
		}
	}

	function getOrderedQuantityByProductId(productId: string): number {
		return cart.value.get(productId)?.orderedQuantity || 0
	}

	watch(
		[items],
		() => {
			saveCartToLocalStorage()
		},
		{ deep: true }
	)

	return {
		items,
		isCartEmpty,
		itemCount,
		subTotalPrice,
		addToCart,
		removeFromCart,
		clearCart,
		updateCartItem,
		initializeCart,
		getOrderedQuantityByProductId,
		canAddToCart,
		errors
	}
})
