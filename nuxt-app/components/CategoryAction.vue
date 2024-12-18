<script setup lang="ts">
const props = defineProps<{viewMode: 'grid' | 'list'}>()
const emit = defineEmits(['update:viewMode'])
const route = useRoute()
const router = useRouter()

const sortQuery = ref(route.query.sort || '')
const priceQuery = ref({
	min: route.query.min || '',
	max: route.query.max || ''
})
const filterQuery = ref(route.query.filter ? 'filter.in-stock' : 'clear')

const sortPolicies = [
	{ id: 'new', title: 'Hàng mới' },
	{ id: 'price-asc', title: 'Giá tăng dần' },
	{ id: 'price-desc', title: 'Giá giảm dần' },
	{ id: 'view', title: 'Xem nhiều' }
]

const filterPolicies = [
	{ id: 'clear', title: 'Lọc sản phẩn' },
	{ id: 'filter.in-stock', title: 'Còn hàng' },
	{ id: 'sort.comment', title: 'Trao đổi' },
	{ id: 'sort.rating', title: 'Đánh giá' },
	{ id: 'sort.name', title: 'Tên A - Z' }
]

const sortInOtherFilter = filterPolicies
	.filter(f => f.id.startsWith('sort'))
	.map(f => f.id.substring('sort.'.length))

function toggleSort(id: string) {
	if (sortQuery.value === id) {
		router.push({
			...route,
			query: {
				...route.query,
				sort: undefined
			}
		})
	} else {
		router.push({
			...route,
			query: {
				...route.query,
				sort: id
			}
		})
	}
}

function applyPrice() {
	router.push({
		...route,
		query: {
			...route.query,
			min: priceQuery.value.min,
			max: priceQuery.value.max
		}
	})
}

function applyOtherFilter(e: Event) {
	const target = e.target as HTMLSelectElement
	const id = target.value
	if (id === 'clear') {
		router.push({
			...route,
			query: {
				...route.query,
				sort: sortInOtherFilter.includes(String(route.query.sort) || '')
					? undefined
					: route.query.sort,
				filter: undefined
			}
		})
	} else {
		const indexOfDot = id.indexOf('.')
		const key = id.substring(0, indexOfDot)
		const value = id.substring(indexOfDot + 1)

		router.push({
			...route,
			query: {
				...route.query,
				[key]: value,
				sort:
					key === 'sort'
						? value
						: sortInOtherFilter.includes(
								String(route.query.sort) || ''
						  )
						? undefined
						: route.query.sort
			}
		})
	}
}

watch(route, route => {
	const { sort, min, max } = route.query
	sortQuery.value = sort || ''
	priceQuery.value = { min: min || '', max: max || '' }
})
</script>
<template>
	<div class="product-show-action-list">
		<div class="action-sort-list">
			<button
				class="btn btn-sort"
				:class="{ active: btn.id === sortQuery }"
				v-for="btn in sortPolicies"
				:key="btn.id"
				@click="toggleSort(btn.id)"
			>
				{{ btn.title }}
			</button>
		</div>

		<form class="action-filter" method="GET" @submit.prevent="applyPrice">
			<input
				class="form-input"
				type="number"
				v-model="priceQuery.min"
				placeholder="Giá thấp nhất"
			/>
			-
			<input
				class="form-input"
				type="number"
				placeholder="Giá cao nhất"
				v-model="priceQuery.max"
			/>
			<button class="btn btn-submit" type="submit">Tìm</button>

			<select
				class="form-select"
				name="filter"
				@change="applyOtherFilter"
				v-model="filterQuery"
			>
				<option :value="item.id" v-for="item in filterPolicies">
					{{ item.title }}
				</option>
			</select>
		</form>

		<div class="action-display">
			<button
				class="icon icon-grid"
				:class="{ active: props.viewMode === 'grid' }"
				@click="emit('update:viewMode', 'grid')"
			></button>
			<button
				class="icon icon-list"
				:class="{ active: props.viewMode === 'list' }"
				@click="emit('update:viewMode', 'list')"
			></button>
		</div>
	</div>
</template>
<style scoped></style>
