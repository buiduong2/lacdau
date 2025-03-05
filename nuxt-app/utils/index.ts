import { childCategories, mainSlicateCategory } from '~/mockData/category'
import { products } from '~/mockData/product'
import type { NavItem } from './types'

export function getSubCategories(): NavItem[] {
	return childCategories.map(c => ({
		...c,
		slug: toSlug(c.name),
		href: toCategoryHref(c),
		level: 0,
		parentId: 0,
		imageSrc: null
	}))
}

export function getMainCategory(): NavItem {
	return {
		...mainSlicateCategory,
		slug: toSlug(mainSlicateCategory.name),
		href: toCategoryHref(mainSlicateCategory),
		parentId: 0,
		imageSrc: null
	}
}

export function getProducts(): ProductSummaryRes[] {
	return products.map(p => ({
		...p
	}))
}
