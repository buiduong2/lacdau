<script setup lang="ts">
import _ from 'lodash'

const props = defineProps<{ totalPage: number }>()
const route = useRoute('category-id')
const router = useRouter()

const activePage = computed<number>(() => Number(route.query.page) || 1)
const siblingCount = 3
const maxCount = 7
const isPositive: Predicate<number> = num => num > 0
const isLessThanOrEqualTotalPage: Predicate<number> = num =>
	num <= props.totalPage
const isGreaterOrEqualLeftBoundary: Predicate<number> = num =>
	num >= activePage.value - siblingCount
const isLessThanOrEqualRightBoundary: Predicate<number> = num =>
	num <= activePage.value + siblingCount
const isGreaterThanOrEqualEndLeft: Predicate<number> = num =>
	num > props.totalPage - maxCount
const isLessThanBeginRight: Predicate<number> = num => num <= maxCount

const isAtStart = () => activePage.value - siblingCount <= 0
const isAtEnd = () => activePage.value + siblingCount >= props.totalPage
const isMiddle = () =>
	activePage.value - siblingCount > 0 &&
	activePage.value + siblingCount < props.totalPage

const activeBtns = computed<number[]>(() => {
	let counts: number[] = [
		..._.range(activePage.value - maxCount, activePage.value),
		activePage.value,
		..._.range(activePage.value + 1, activePage.value + maxCount + 1)
	]

	if (isAtStart()) {
		return counts.filter(
			num =>
				isPositive(num) &&
				isLessThanBeginRight(num) &&
				isLessThanOrEqualTotalPage(num)
		)
	}

	if (isAtEnd()) {
		return counts.filter(
			num =>
				isPositive(num) &&
				isGreaterThanOrEqualEndLeft(num) &&
				isLessThanOrEqualTotalPage(num)
		)
	}

	if (isMiddle()) {
		return counts.filter(
			num =>
				isGreaterOrEqualLeftBoundary(num) &&
				isLessThanOrEqualRightBoundary(num)
		)
	}

	// isTotalPage <= maxCount
	return counts.filter(
		num => isPositive(num) && isLessThanOrEqualTotalPage(num)
	)
})

function changePage(number: number) {
	if (number === activePage.value) return

	router.push({
		path: route.path,
		query: {
			...route.query,
			page: number
		}
	})
}
</script>
<template>
	<div class="product-pagination">
		<button
			class="btn btn-pagination"
			:class="{ active: btn === activePage }"
			v-for="btn in activeBtns"
			:key="btn"
			@click="changePage(btn)"
			data-test="btns"
		>
			{{ btn }}
		</button>
	</div>
</template>

<style scoped></style>
