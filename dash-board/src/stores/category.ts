import type { Category } from '@/types/categories/resTypes'
import { buildTree, type TreeNode } from '@/utils'

import { acceptHMRUpdate, defineStore } from 'pinia'

export interface CategoryTree extends TreeNode {
  name: string
  level: number
  children?: CategoryTree[]
}

export const useCategoryStore = defineStore('category-store', () => {
  const categoies = ref<Category[]>([])
  let categoryTrees = shallowRef<CategoryTree[]>([])
  let isInited = false
  let root: CategoryTree | undefined = undefined

  async function fetchInit() {
    if (!isInited) {
      const data = await fetchCategories()
      categoies.value = data
      root = buildTree(data!) as CategoryTree
      categoryTrees.value = root.children as CategoryTree[]
      isInited = true
    }
    return
  }

  function findTreeById(id: number): CategoryTree | null {
    return dfs<CategoryTree>(root!, (node) => node.id === id)
  }

  function setDirty() {
    isInited = false
  }

  return {
    categoies,
    fetchInit,
    setDirty,
    categoryTrees: categoryTrees,
    findTreeById,
  }
})

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useCategoryStore, import.meta.hot))
}
