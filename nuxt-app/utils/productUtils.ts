interface Saleable {
	salePrice: number | null
	originalPrice: number
}
export function computedDiscountPercent(saleable: Saleable): number {
	const { salePrice, originalPrice } = saleable
	if (!salePrice) return 0
	return 100 - Math.ceil((salePrice / originalPrice) * 100)
}
