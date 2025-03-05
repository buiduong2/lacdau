import type { RouteCreateable, RouteEditable } from '@/types'
import type { Router } from 'vue-router'

type DropDownItem = {
  text: string
  icon: string
  click: (id: number | string) => void
  color?: 'red'
}

type GroupItemClick = DropDownItem & { type: 'custom' }
type GroupItemRouter = {
  url: RouteEditable
  text: string
  icon: string
  type: 'router'
}

type GroupItem = GroupItemClick | GroupItemRouter

type Options = {
  onDelete?: (id: number | string) => void
  removeByIdIn: (id: number[] | string[], done: () => void) => void
  onCreate: RouteCreateable
  onEdit?: RouteEditable
  groupTop?: GroupItem[]
  groupBottom?: GroupItem[]
}

export type TableAction = {
  createUrl: string
  removeByIdIn: (id: number[] | string[], done: () => void) => void
  dropdowns: DropDownItem[][]
}

function tranformToDropDownItem(item: GroupItem, router: Router): DropDownItem {
  if (item.type === 'custom') {
    return item
  }

  return {
    icon: item.icon,
    text: item.text,
    click: (id: number | string) => router.push({ name: item.url, params: { id } }),
  }
}

export function useTableAction(options: Options): TableAction {
  const { onCreate, onDelete, onEdit, groupBottom = [], groupTop = [], removeByIdIn } = options
  const router = useRouter()
  const dropdowns: DropDownItem[][] = []
  if (onEdit) {
    dropdowns.push([
      {
        text: 'Chỉnh sửa',
        icon: 'lucide:edit',
        click: (id) => router.push({ name: onEdit, params: { id } }),
      },
    ])
  }
  if (groupTop) {
    if (dropdowns.length === 0) {
      dropdowns.push([...groupTop.map((i) => tranformToDropDownItem(i, router))])
    } else {
      dropdowns[0].push(...groupTop.map((i) => tranformToDropDownItem(i, router)))
    }
  }

  if (onDelete) {
    dropdowns.push([
      { text: 'Xóa', icon: 'lucide:trash', click: onDelete, color: 'red' },
      ...groupBottom.map((i) => tranformToDropDownItem(i, router)),
    ])
  }
  return {
    createUrl: onCreate,
    removeByIdIn,
    dropdowns,
  }
}
