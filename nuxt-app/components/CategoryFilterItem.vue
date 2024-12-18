<script setup lang="ts">
import { Icon } from '@iconify/vue/dist/iconify.js';
import type { CategoryFilterOption } from '~/utils/types';

const props = defineProps({
	filter: {
		required: true,
		type: Object as PropType<Filter>
	},
	limit: {
		type: Number,
		default: 5
	}
})

const route = useRoute()
const router = useRouter()

const optionsObj: Record<string, Filter['options'][0]> = {}
props.filter.options.forEach(o => {
	optionsObj[o.id] = o
})
const isOverLimit = props.filter.options.length > props.limit
const isExpanded = ref(false)


const options = computed<CategoryFilterOption[]>(() => {
	if (isOverLimit && !isExpanded.value) {
		return props.filter.options.slice(0, props.limit)
	}
	return props.filter.options
})


const query = computed(() => route.query[props.filter.id])

const pickedOption = computed<CategoryFilterOption | undefined>(() => {
	if (!query.value) return undefined
	if (typeof query.value === 'string') {
		return optionsObj[query.value]
	} else if (Array.isArray(query.value)) {
		for (const q of query.value) {
			if (optionsObj[String(q)]) return optionsObj[String(q)]
		}
	}
})

function selectOption(id: string) {
	router.push({
		...route,
		query: {
			...route.query,
			[props.filter.id]: [...(route.query[props.filter.id] || []), id]
		}
	})
}

function deselectOption(id: string) {
	const group = props.filter.id
	if (!route.query[group]) throw new Error('Error found option not exists')

	let existedQuery = route.query[group]
	if (typeof existedQuery === 'string') {
		existedQuery = [existedQuery]
	}
	let finalQuery = existedQuery.filter(q => q !== id)

	router.push({
		...route,
		query: {
			...route.query,
			[group]: finalQuery
		}
	})
}
</script>
<template>
	<div class="product-filter-item">
		<h3 class="filter-title">{{ filter.label }}</h3>
		<ul class="filter-list">
			<li v-if="pickedOption" class="filter-item">
				<a
					class="filter-link checked"
					href="#"
					@click.prevent="deselectOption(pickedOption.id)"
				>
					<i class="icon-square"
						><Icon icon="fa-check" color="white" />
					</i>
					{{ pickedOption.label }}
				</a>
			</li>

			<li
				v-else
				class="filter-item"
				v-for="option in options"
				:key="option.id"
			>
				<a
					class="filter-link"
					href="#"
					@click.prevent="selectOption(option.id)"
				>
					<i class="icon-square"></i> {{ option.label }}
				</a>
			</li>
		</ul>

		<div class="filter-action" v-if="isOverLimit && !pickedOption">
			<button class="btn" @click="isExpanded = !isExpanded">
				{{ isExpanded ? 'Thu gọn' : 'Xem Thêm' }}
				<Icon
					:icon="isExpanded ? 'fa-chevron-up' : 'fa-chevron-down'"
				/>
			</button>
		</div>
	</div>
</template>

<style scoped></style>
