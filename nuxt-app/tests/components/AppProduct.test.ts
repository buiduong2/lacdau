import { mountSuspended } from '@nuxt/test-utils/runtime'
import AppProduct from '~/components/AppProduct.vue'
import { describe, expect, it } from 'vitest'

const product: ProductSummaryRes = {
	id: 'PADDA0042',
	name: 'LÓT CHUỘT DA 27X24 MÀU ĐEN (1 MẶT/1MM)',
	mainImage: null,
	originalPrice: 30000,
	salePrice: 10000,
	quantity: 1
}

const expecteHref = '/product/PADDA0042'

describe('AppProductItem', () => {
	it('Should render', async () => {
		const comp = await mountSuspended(AppProduct, {
			props: { product }
		})
		const title = comp.get('[data-test="product-title"]')
		const id = comp.get('[data-test="product-id"]')
		const img = comp.get('[data-test="product-img"]')
		const anchors = comp.findAll('[data-test="product-hrefs"]')

		expect(title.text()).toBe(product.name)
		expect(id.text()).toBe('Mã: ' + product.id)
		expect(img.isVisible()).toBe(true)

		expect(anchors).toHaveLength(2)
		expect(
			anchors.map(a => a.attributes('href')).every(h => h === expecteHref)
		).toBe(true)
	})
})

describe('AppProductItem With State', () => {
	it('Should render Out of stock', async () => {
		const comp = await mountSuspended(AppProduct, {
			props: { product: { ...product, quantity: 0 } }
		})
		const state = comp.get('[data-test="product-state"]')
		expect(state.text()).toBe('Hết hàng')
		expect(state.classes()).toContain('state--out')
	})

	it('Should render avaiable ', async () => {
		const comp = await mountSuspended(AppProduct, {
			props: { product: { ...product, quantity: 10 } }
		})
		const state = comp.get('[data-test="product-state"]')
		expect(state.text()).toBe('Còn hàng')
		expect(state.classes()).not.toContain('state--out')
	})
})

describe('AppProduct With Price', () => {
	it('should render bold active and old Price', async () => {
		const comp = await mountSuspended(AppProduct, {
			props: { product: { ...product } }
		})
		const activePrice = comp.get('[data-test="product-active-price"]')
		const oldPrice = comp.get('[data-test="product-old-price"]')

		expect(activePrice.isVisible()).toBe(true)
		expect(oldPrice.isVisible()).toBe(true)
	})

	it('Should reduce Price for discount', async () => {
		const comp = await mountSuspended(AppProduct, {
			props: { product: { ...product } }
		})
		const activePrice = comp.get('[data-test="product-active-price"]')
		expect(activePrice.text()).toBe(String(product.salePrice) + 'đ')
	})

	it('should render Only ACtive Price', async () => {
		const comp = await mountSuspended(AppProduct, {
			props: { product: { ...product, salePrice: null } }
		})
		const activePrice = comp.get('[data-test="product-active-price"]')
		const oldPrice = comp.find('[data-test="product-old-price"]')

		expect(activePrice.isVisible()).toBe(true)
		expect(oldPrice.exists()).toBe(false)
	})
})
