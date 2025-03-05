import type { ProductQuery } from './types'

interface Saleable {
	salePrice: number | null
	originalPrice: number
}
interface TotalPriceAble {
	salePrice: number | null
	originalPrice: number
}

const dateFormat = new Intl.DateTimeFormat('vi-VN', {
	year: '2-digit',
	month: '2-digit',
	day: '2-digit',
	hour: '2-digit',
	minute: '2-digit',
	second: '2-digit'
})

export function formatDate(dateStr: string | null | undefined): string {
	if (!dateStr) {
		return '-'
	}
	return dateFormat.format(new Date(dateStr))
}

export function formatCurrencyVND(amount?: number | null) {
	if (!amount && amount !== 0) {
		return '-'
	}
	return new Intl.NumberFormat('vi-VN', {
		style: 'currency',
		currency: 'VND'
	}).format(amount)
}

export function getFinalPrice(product: Saleable) {
	return product.salePrice ?? product.originalPrice
}

export function computedTotalPrice(
	product: TotalPriceAble,
	quantity: number
): number {
	return (product.salePrice ?? product.originalPrice) * quantity
}

export function computedDiscountPercent(saleable: Saleable): number {
	const { salePrice, originalPrice } = saleable
	if (!salePrice) return 0
	return 100 - Math.ceil((salePrice / originalPrice) * 100)
}

export function serializeProductQuery(query: ProductQuery): string {
	return [
		`page=${query.page}`,
		`categoryId=${query.categoryId}`,
		query.brandId ? `brandId=${query.brandId}` : '',
		query.min ? `min=${query.min}` : '',
		query.max ? `max=${query.max}` : '',
		query.filterIds?.length ? `filterIds=${query.filterIds.join(',')}` : ''
	]
		.filter(Boolean)
		.join('&')
}
