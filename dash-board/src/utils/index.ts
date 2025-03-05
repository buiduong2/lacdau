const numberFormat = new Intl.NumberFormat('vi-VN', {
  style: 'currency',
  currency: 'VND',
})

const dateFormat = new Intl.DateTimeFormat('vi-VN', {
  year: '2-digit',
  month: '2-digit',
  day: '2-digit',
  hour: '2-digit',
  minute: '2-digit',
  second: '2-digit',
})

export function formartCurrency(num?: number | null): string {
  return num ? numberFormat.format(num) : '-'
}

export function formatDate(dateStr: string): string {
  return dateFormat.format(new Date(dateStr))
}

export interface TreeNode {
  id: number
  heigth?: number
  level?: number
  parent?: TreeNode
  parentId?: number | null
  children?: TreeNode[]
}

export function buildTree<T extends TreeNode>(nodes: T[]): TreeNode {
  const root: TreeNode = {
    id: 0,
    children: [],
  }
  const map: Map<TreeNode['id'], TreeNode> = new Map()
  map.set(0, root)
  nodes.forEach((node) => map.set(node.id, node))

  nodes.forEach((node) => {
    if (node.parentId) {
      const parent = map.get(node.parentId)
      if (parent) {
        if (!parent.children) {
          parent.children = []
        }
        parent.children.push(node)
        node.parent = parent
      }
    } else {
      root.children?.push(node)
    }
  })
  buildDepthLevel(root)
  buildHeighLevel(root)
  return root as T
}

function buildDepthLevel(node: TreeNode, level: number = -1): void {
  node.level = level

  node.children?.forEach((child) => buildDepthLevel(child, level + 1))
}

function buildHeighLevel(node: TreeNode): void {
  if (!node.children) {
    node.heigth = 0
    return
  }

  node.children.forEach((child) => {
    buildHeighLevel(child)
  })

  node.heigth = Math.max(...node.children.map((child) => child.heigth || 0)) + 1
}

export function findPath<T extends TreeNode>(nodes: T[], predicate: Predicate<T>): T[] {
  const res: T[] = []

  let target = null

  for (const node of nodes) {
    target = dfs(node, predicate)
    if (target) {
      break
    }
  }
  if (target) {
    res.unshift(target)
    while (target.parent) {
      target = target.parent as T
      res.unshift(target)
    }
  }

  return res
}

export function dfs<T extends TreeNode>(root: T, predicate: Predicate<T>): T | null {
  if (predicate(root)) {
    return root
  }

  if (!root.children) return null
  for (const element of root.children) {
    const res = dfs(element as T, predicate)
    if (res) {
      return res
    }
  }

  return null
}

export function toSearchParams(key: string, arr: Array<any>): string {
  const searchParams = new URLSearchParams()
  arr.forEach((i) => searchParams.append(key, i))
  return searchParams.toString()
}

type Predicate<T> = (t: T) => boolean

export function debounce<T extends (...args: any[]) => void>(
  wait: number,
  callback: T,
  immediate = false,
) {
  let timeout: ReturnType<typeof setTimeout> | null

  return function <U>(this: U, ...args: Parameters<typeof callback>) {
    const context = this
    const later = () => {
      timeout = null

      if (!immediate) {
        callback.apply(context, args)
      }
    }
    const callNow = immediate && !timeout

    if (typeof timeout === 'number') {
      clearTimeout(timeout)
    }

    timeout = setTimeout(later, wait)

    if (callNow) {
      callback.apply(context, args)
    }
  }
}
export function calculateExpirationDate(expiresIn: number): Date {
  const now = new Date()
  const expirationDate = new Date(now.getTime() + expiresIn * 1000)
  return expirationDate
}

export function isBlank(str: string) {
  return !(str && str.trim().length > 0)
}
