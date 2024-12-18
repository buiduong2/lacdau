import _ from 'lodash'

export function toSlug(str: string): string {
	return _.kebabCase(_.deburr(str))
}

export function computedFinalPrice(
	original: number,
	discountPercent: number = 0
) {
	return original - (original * discountPercent) / 100
}

interface Child<T = unknown>  {
	id: ID
	parent?: T
}

export function getAncestors<T extends Child<T>>(child: T): T[] {
	const result: T[] = [child]

	let currentChild: T = child

	while (currentChild.parent) {
		result.unshift(currentChild.parent)
		currentChild = currentChild.parent
	}

	return result 
}

interface Node<T> {
	id: T
	children?: Node<T>[]
	parentId: T | null
}

export function dfs<T extends Node<T['id']>>(
	nodes: T[],
	targetId: T['id']
): T | null

export function dfs<T extends Node<T['id']>>(
	node: T,
	targetId: T['id']
): T | null

export function dfs<T extends Node<T['id']>>(
	node: T[] | T,
	targetId: T['id']
): T | null {
	if (Array.isArray(node)) {
		for (const n of node) {
			const result = dfs(n, targetId)
			if (result) return result
		}
	} else {
		if (node.id === targetId) {
			return node
		}

		if (node.children) {
			for (const child of node.children as T[]) {
				const result = dfs(child, targetId)
				if (result) return result
			}
		}
	}
	return null
}
