import { Authority } from '@/types/auth/resTypes'
import type { TableFacetedFilterInput, TableFacetedFilterPick } from '../../app/table/types'

export async function getFilters() {
  const filterInputs: TableFacetedFilterInput[] = [
    {
      name: 'displayName',
      placeholder: 'Tìm kiếm theo tên',
      label: 'Tên',
    },
    {
      name: 'email',
      placeholder: 'Tìm kiếm theo Email',
      label: 'Email',
    },
  ]

  const filterPicks: TableFacetedFilterPick[] = [
    {
      columnId: 'permissions',
      options: Object.values(Authority)
        .filter((auth) => typeof auth === 'string')
        .map((value) => ({
          label: String(value),
          value: String(value),
        })),
      title: 'Quền hạn',
    },
  ]

  return {
    filterInputs,
    filterPicks,
  }
}
