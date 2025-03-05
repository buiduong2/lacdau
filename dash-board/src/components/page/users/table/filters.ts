import type { TableFacetedFilterInput, TableFacetedFilterPick } from '../../app/table/types'

export async function getFilters() {
  const filterInputs: TableFacetedFilterInput[] = [
    {
      name: 'displayName',
      placeholder: 'Tìm kiếm theo tên',
      label: 'Tên',
    },
    {
      name: 'username',
      placeholder: 'Tìm kiếm theo username',
      label: 'Username',
    },
  ]

  const filterPicks: TableFacetedFilterPick[] = [
    {
      columnId: 'morderates',
      options: [
        {
          label: 'Người dùng',
          value: 'user',
        },
        {
          label: 'Quản trị viên',
          value: 'admin',
        },
      ],
      title: 'Người dùng',
    },
    {
      columnId: 'providers',
      options: [
        {
          label: 'Hệ thống',
          value: 'SYS',
        },
        {
          label: 'Google',
          value: 'OAUTH-GOOGLE',
        },
        {
          label: 'Github',
          value: 'OAUTH-GITHUB',
        },
      ],
      title: 'Nhà cung cấp',
    },
  ]

  return { filterInputs, filterPicks }
}
