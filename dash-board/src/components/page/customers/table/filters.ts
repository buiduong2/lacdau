import type { TableFacetedFilterInput, TableFacetedFilterPick } from '../../app/table/types'

export async function getFilters() {
  const filterInputs: TableFacetedFilterInput[] = [
    {
      name: 'id',
      placeholder: 'Tìm kiếm theo ID',
      label: 'ID',
    },
    {
      name: 'name',
      placeholder: 'Tìm kiếm theo tên',
      label: 'Tên',
    },

    {
      name: 'addressProvince',
      placeholder: 'Tp.Hà Nội',
      label: 'Đỉa chỉ',
    },
  ]

  return { filterInputs }
}
