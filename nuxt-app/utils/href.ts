export function toCategoryHref<T extends ToHrefAble>(category: T): string {
	return `/category/${category.id}`
}

export function toProductHref<T extends ToHrefAble>(product: T): string {
	return `/product/${product.id}`
}
