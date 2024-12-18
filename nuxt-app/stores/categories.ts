import { defineStore } from 'pinia'

export const useCategoryStore = defineStore('categories', () => {
	let navList = shallowRef<NavItem[]>([])

	function getNavList() {
		return navList.value
	}

	async function fetch() {
		const { data } = await useAsyncData('categories', () =>
			$fetch('/api/categories')
		)
		if (data.value == null) {
			return
		}

		navList.value = toNavList(data.value, cat => cat.parentId == null)
	}

	function getCategoryById(id: number): NavItem | null {
		return dfs(navList.value, id)
	}

	function getBreadcrumbById(id: number): Breadcrumb[] {
		const navItem = getCategoryById(id)

		if (navItem) {
			return getAncestors(navItem).map(nav => ({
				...nav,
				href: toCategoryHref(nav)
			}))
		}
		return []
	}

	return {
		getBreadcrumbById,
		getNavList,
		fetch,
		getCategoryById
	}
})
