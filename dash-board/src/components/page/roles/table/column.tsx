import DataTableColumn from '@/components/page/app/table/AppDataTableColumn.vue'
import { Badge } from '@/components/ui/badge'
import type { UserPermission } from '@/types/roles/resTypes'

import type { ColumnDef } from '@tanstack/vue-table'

export const columns: ColumnDef<UserPermission>[] = [
  {
    accessorKey: 'id',
    header: ({ column }) => <DataTableColumn column={column} title="ID" />,
    cell: ({ row }) => <div class="text-left">{row.original.id}</div>,
  },
  {
    accessorKey: 'displayName',
    header: ({ column }) => <DataTableColumn column={column} title="Tên" />,
    cell: ({ row }) => <div class="text-left">{row.original.displayName}</div>,
  },
  {
    accessorKey: 'email',
    header: ({ column }) => <DataTableColumn column={column} title="Email" />,
    cell: ({ row }) => <div class="text-left">{row.original.email}</div>,
  },
  {
    accessorKey: 'permissions',
    header: ({ column }) => <DataTableColumn column={column} title="Quyền hạn" />,
    cell: ({ row }) => (
      <div class="text-left flex gap-2 flex-wrap">
        {row.original.permissions.map((p) => (
          <Badge variant={p.revokedAt ? 'destructive' : 'default'}>{p.permission}</Badge>
        ))}
      </div>
    ),
    filterFn: (row, id, value) => {
      if (value === undefined) {
        return true
      }

      if (Array.isArray(value)) {
        value = value.map((value) => (value === -1 ? null : value))
        const rowPerrmions = row
          .getValue<UserPermission['permissions']>(id)
          .map((p) => p.permission)

        return value.every((v: any) => rowPerrmions.includes(v))
      }

      throw new Error('Value must be an array')
    },
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
