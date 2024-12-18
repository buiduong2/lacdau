import DataTableColumn from '@/components/page/app/table/AppDataTableColumn.vue'
import type { ColumnDef } from '@tanstack/vue-table'
import { filterPickFn } from '../../app/table/utils'
import type { Category } from '@/types/categories/resTypes'

export const columns: ColumnDef<Category>[] = [
  {
    accessorKey: 'id',
    header: ({ column }) => <DataTableColumn column={column} title="ID" />,
    cell: ({ row }) => <div class="text-left">{row.original.id}</div>,
  },
  {
    accessorKey: 'imageSrc',
    header: ({ column }) => <DataTableColumn column={column} title="Hình ảnh" />,
    cell: ({ row }) => (
      <img class="size-20 rounded-lg" src={row.original.imageSrc || '/placeholder.svg'} />
    ),
  },
  {
    accessorKey: 'name',
    header: ({ column }) => <DataTableColumn column={column} title="Tên" />,
    cell: ({ row }) => <div class="text-left">{row.original.name}</div>,
  },
  {
    accessorKey: 'level',
    header: ({ column }) => (
      <DataTableColumn column={column} title="Độ sâu" class="justify-center" />
    ),
    cell: ({ row }) => <div class="text-center">{row.original.level}</div>,
    filterFn: filterPickFn,
  },
  {
    accessorKey: 'parentId',
    header: ({ column }) => (
      <DataTableColumn column={column} title="Id cấp trên" class="justify-center" />
    ),
    cell: ({ row }) => <div class="text-center">{row.original.parentId || '-'}</div>,
  },
  {
    accessorKey: 'createdAt',
    header: ({ column }) => (
      <DataTableColumn class="justify-center" column={column} title="Ngày tạo" />
    ),
    cell: ({ row }) => {
      return <div class="text-center">{formatDate(row.original.createdAt)}</div>
    },
  },
  {
    accessorKey: 'updatedAt',
    header: ({ column }) => (
      <DataTableColumn class="justify-center" column={column} title="Ngày cập nhật" />
    ),
    cell: ({ row }) => {
      return <div class="text-center">{formatDate(row.original.updatedAt)}</div>
    },
  },
]
