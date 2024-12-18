import DataTableColumn from '@/components/page/app/table/AppDataTableColumn.vue'
import type { Attribute } from '@/types/attributes/resTypes'
import type { ColumnDef } from '@tanstack/vue-table'

export const columns: ColumnDef<Attribute>[] = [
  {
    accessorKey: 'id',
    header: ({ column }) => <DataTableColumn column={column} title="ID" />,
    cell: ({ row }) => <div class="text-left">{row.original.id}</div>,
  },
  {
    accessorKey: 'name',
    header: ({ column }) => <DataTableColumn column={column} title="Tên" />,
    cell: ({ row }) => <div class="text-left">{row.original.name}</div>,
  },
  {
    accessorKey: 'attributeValueCount',
    header: ({ column }) => <DataTableColumn column={column} title="Số lượng thược tính" />,
    cell: ({ row }) => <div class="text-left">{row.original.attributeValueCount}</div>,
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
