import type { RouteLocationRaw } from 'vue-router'

export type ID = number

export type DeepPartial<T> = {
	[K in keyof T]?: T[K] extends object ? DeepPartial<T[K]> : T[K]
}

export interface SimpleCategory {
	id: ID
	name: string
	parentId: ID | null
}

export interface Category extends SimpleCategory {
	slug: string
	href: string
}

export type ProductQuery = {
	page: number
	categoryId: number
	brandId?: number
	min?: number
	max?: number
	filterIds?: number[]
}

export interface Filter {
	id: string
	label: string
	options: {
		id: string
		label: string
	}[]
}

export interface SortPolicy {
	id: string
	title: string
}

export interface OtherFilter extends SortPolicy {}

export interface ToHrefAble {
	id: ID
	name: string
}

export interface NavItem extends CategoryRes {
	href: string
	slug: string
	icon?: string
	children?: NavItem[]
	parent?: NavItem
}

export interface Breadcrumb {
	id: number | string
	name: string
	href: RouteLocationRaw | string
}

export type Predicate<T> = (t: T) => boolean

// Category Page

export type CategoryFilter = {
	id: string
	label: string
	options: {
		id: string
		label: string
	}[]
}

export type CategoryFilterOption = CategoryFilter['options'][0]

// FETCH
