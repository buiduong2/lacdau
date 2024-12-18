import _ from 'lodash'

export const navIcons = [
	'icon-cat cat-keyboard',
	'icon-cat cat-controller',
	'icon-cat cat-screen',
	'icon-cat cat-box',
	'icon-cat cat-lamp',
	'icon-cat cat-pump',
	'icon-cat cat-chair',
	'icon-cat cat-table',
	'icon-cat cat-usb',
	'icon-cat cat-fan',
	'icon-cat'
]

export function toNavList(
	categories: CategoryRes[],
	rootPredicate?: (e: CategoryRes) => boolean
): NavItem[] {
	//Mapping New

	const flatNavItems: NavItem[] = categories.map(cat => ({
		...cat,
		children: undefined,
		icon: undefined,
		href: toCategoryHref(cat),
		slug: toSlug(cat.name)
	}))

	const groupByParent = _.groupBy(flatNavItems, nav => nav.parentId)

	flatNavItems.forEach(nav => {
		nav.children = groupByParent[nav.id]
		nav.children?.forEach(child => (child.parent = nav))
	})

	let predicate = rootPredicate || (nav => nav.parentId === null)
	const roots = flatNavItems.filter(predicate)

	roots.forEach((nav, index) => {
		nav.icon = navIcons[index]
	})

	return roots
}
