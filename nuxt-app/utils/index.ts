import _ from 'lodash'
import { childCategories, mainSlicateCategory } from '~/mockData/category'
import { navData, navIcons } from '~/mockData/navbar'
import { products } from '~/mockData/product'
import type { Breadcrumb, NavItem, Product, SimpleCategory } from './types'

function toNavList(
	categories: SimpleCategory[],
	rootPredicate?: (e: SimpleCategory) => boolean
): NavItem[] {
	const flatNavItems: NavItem[] = categories.map(cat => ({
		...cat,
		children: undefined,
		icon: undefined,
		imageSrc: null,
		href: toCategoryHref(cat),
		slug: toSlug(cat.name)
	}))

	const groupByParent = _.groupBy(flatNavItems, nav => nav.parentId)

	flatNavItems.forEach(nav => {
		nav.children = groupByParent[nav.id]
	})

	let predicate = rootPredicate || (nav => nav.parentId === null)
	const roots = flatNavItems.filter(predicate)

	roots.forEach((nav, index) => {
		nav.icon = navIcons[index]
	})

	return roots
}

export function getNavList(): NavItem[] {
	return toNavList(JSON.parse(JSON.stringify(navData)))
}

export function getCategories(): NavItem[] {
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
		level: 0,
		parentId: 0,
		imageSrc: null
	}
}

export function getProducts(): Product[] {
	return products.map(p => ({
		...p,
		slug: toSlug(p.name),
		href: toProductHref(p)
	}))
}

export function getCategoryById(id: ID): NavItem | null {
	const category = navData.find(cat => cat.id == id)
	category
	if (!category) return null
	const children = navData.filter(cat => cat.parentId === id)
	return toNavList([category, ...children], root => root.id === id)[0]
}

// export function getBreadcrumbs(category: SimpleCategory | null): Breadcrumb[] {
// 	if (!category) return []
// 	return getAncestors(category, navData).map(cat => ({
// 		...cat,
// 		title: cat.name,
// 		href: toCategoryHref(cat)
// 	}))
// }
