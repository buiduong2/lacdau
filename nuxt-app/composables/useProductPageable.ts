import type { LocationQuery } from 'vue-router'

interface Route {
	query: LocationQuery
}

interface InternalPolicy extends SortPolicy {
	query: { sort?: string }
}

const sortPolicies: InternalPolicy[] = [
	{ id: 'new', title: 'Hàng mới', query: { sort: 'createdAt,asc' } },
	{ id: 'price-asc', title: 'Giá tăng dần', query: { sort: 'price,asc' } },
	{ id: 'price-desc', title: 'Giá giảm dần', query: { sort: 'price,desc' } },
	{ id: 'view', title: 'Xem nhiều', query: { sort: 'viewCount,desc' } }
]

const filterPolicies: InternalPolicy[] = [
	{ id: 'clear', title: 'Lọc sản phẩn', query: {} },
	{
		id: 'filter.in-stock',
		title: 'Còn hàng',
		query: { sort: 'quantity,desc' }
	},
	{ id: 'sort.comment', title: 'Trao đổi', query: {} },
	{ id: 'sort.rating', title: 'Đánh giá', query: {} },
	{ id: 'sort.name', title: 'Tên A - Z', query: { sort: 'name,asc' } }
]

export async function useProductPageable(categoryId: number, route: Route) {
	const { data } = await useFetch<FilterRes>(
		`/api/products/filter/${categoryId}`
	)

	const filterQuery = computed<ProductQuery>(() => {
		const query = {
			categoryId: categoryId,
			page: Number(route.query.page) - 1 || 0
		}

		const brandId = Number(route.query.brand) || undefined

		const price = {
			...(toPriceParam(String(route.query.price)) || {})
		}
		let filterIds: number[] = []

		const filterQuery = route.query.filter
		if (!Array.isArray(filterQuery)) {
			filterIds = [Number(filterQuery)]
		} else {
			filterIds = [...filterQuery].map(Number)
		}

		filterIds = filterIds.filter(num => !isNaN(num))

		return { ...query, brandId, ...price, filterIds }
	})

	const sortQuery = computed(() => {
		if (route.query.sort && !Array.isArray(route.query.sort)) {
			const policy = findPolicySortByKey(route.query.sort)
			if (policy) {
				return {
					sort: policy.query.sort
				}
			}
		}
		return {}
	})

	const query = computed(() => {
		return { ...filterQuery.value, ...sortQuery.value }
	})

	function toPriceParam(
		queryStr?: string
	): Partial<{ min: number; max: number }> | undefined {
		if (!queryStr) return
		const [min, max] = queryStr.split(',')

		if (isNaN(Number(min)) && isNaN(Number(max))) {
			return undefined
		}

		return { min: Number(min) || 0, max: Number(max) || undefined }
	}

	function toFilters(): Filter[] {
		if (!data.value) {
			return []
		}

		const { brand, price, attributes } = data.value

		const filters: Filter[] = [
			{
				id: 'brand',
				label: 'HÃNG SẢN XUẤT',
				options: brand.attributeValues.map(attr => ({
					id: String(attr.id),
					label: attr.name
				}))
			},
			{
				id: 'price',
				label: 'KHOẢNG GIÁ',
				options: toPriceOptions(price)
			}
		]

		if (attributes && attributes.length) {
			filters.push(
				...attributes.map(attr => ({
					id: 'filter',
					label: attr.name,
					options: attr.attributeValues.map(value => ({
						id: String(value.id),
						label: value.name
					}))
				}))
			)
		}

		return filters
	}

	return {
		filters: toFilters(),
		sortPolicies: sortPolicies,
		filterPolicies: filterPolicies,
		toPriceParam,
		query
	}
}

function toPriceOptions(price: {
	min: number
	max: number
}): Filter['options'] {
	const { min, max } = price
	const range: Filter['options'] = [
		{ id: '100000,', label: 'Dưới 100 ngàn' },
		{ id: '100000,200000', label: '100 ngàn - 200 ngàn' },
		{ id: '200000,500000', label: '200 ngàn - 500 ngàn' },
		{ id: '500000,1000000', label: '500 ngàn - 1 triệu' },
		{ id: '1000000,2000000', label: '1 triệu - 2 triệu' },
		{ id: '2000000,5000000', label: '2 triệu - 5 triệu' },
		{ id: ',5000000', label: 'Trên 5 triệu' }
	]

	const maxValues: number[] = [
		0,
		200000,
		500000,
		1000000,
		2000000,
		5000000,
		Number.MAX_VALUE
	]
	// max = 650.000
	let minIndex = 0
	let maxIndex = 0

	for (let index = 0; index < maxValues.length; index++) {
		if (maxValues[index] <= min) {
			minIndex = index
		}

		if (maxValues[index] <= max) {
			maxIndex = index
		} else {
			maxIndex = index
			break
		}
	}

	return range.slice(minIndex, maxIndex)
}

function findPolicySortByKey(key: string): InternalPolicy | undefined {
	return (
		sortPolicies.find(p => p.id === key) ||
		filterPolicies
			.filter(f => f.id.startsWith('sort.'))
			.find(f => f.id.substring('sort.'.length) === key)
	)
}
