import DataTableColumn from '@/components/page/app/table/AppDataTableColumn.vue'
import { Badge, type BadgeVariants } from '@/components/ui/badge'
import type { User } from '@/types/users/resTypes'
import { Icon } from '@iconify/vue/dist/iconify.js'

import type { ColumnDef } from '@tanstack/vue-table'

export const columns: ColumnDef<User>[] = [
  {
    accessorKey: 'id',
    header: ({ column }) => <DataTableColumn column={column} title="ID" />,
    cell: ({ row }) => <div class="text-left">{row.original.id}</div>,
  },
  {
    accessorKey: 'avatarUrl',
    header: ({ column }) => <DataTableColumn column={column} title="Avatar" />,
    cell: ({ row }) => (
      <img class="size-20 rounded-lg" src={row.original.avatarUrl || '/placeholder.svg'} />
    ),
  },
  {
    accessorKey: 'displayName',
    header: ({ column }) => <DataTableColumn column={column} title="Tên" />,
    cell: ({ row }) => <div class="text-left">{row.original.displayName}</div>,
  },

  {
    accessorKey: 'username',
    header: ({ column }) => <DataTableColumn column={column} title="Tên tài khoản" />,
    cell: ({ row }) => <div class="text-left">{row.original.username}</div>,
  },
  {
    accessorKey: 'email',
    header: ({ column }) => <DataTableColumn column={column} title="Email" />,
    cell: ({ row }) => <div class="text-left text-wrap">{row.original.email}</div>,
  },
  {
    accessorKey: 'emailVerified',
    header: ({ column }) => <DataTableColumn column={column} title="Xác thực" />,
    cell: ({ row }) => (
      <div class="flex justify-center">
        {row.original.emailVerified ? (
          <Icon icon="lucide:circle-check" color="green" class="size-5" />
        ) : (
          <Icon icon="lucide:circle-x" color="red" class="size-5" />
        )}
      </div>
    ),
  },

  {
    accessorKey: 'userType',
    header: ({ column }) => (
      <DataTableColumn column={column} title="Người dùng " class="justify-center" />
    ),
    cell: ({ row }) => (
      <Badge
        variant={row.original.moderate ? 'default' : 'outline'}
        class="font-medium text-center"
      >
        {row.original.moderate ? 'Admin' : 'Người dùng'}
      </Badge>
    ),
  },
  {
    accessorKey: 'provider',
    header: ({ column }) => (
      <DataTableColumn column={column} title="Nhà cung cấp" class="justify-center" />
    ),
    cell: ({ row }) => (
      <Badge variant={getUserTypeVariant(row.original)} class="font-medium text-center">
        {getUserTypeLabel(row.original)}
      </Badge>
    ),
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
  {
    id: 'morderates',
  },
  { id: 'providers' },
]

export function getUserTypeVariant(user: User): BadgeVariants['variant'] {
  switch (user.userType) {
    case 'OAUTH':
      return user.provider === 'GOOGLE' ? 'destructive' : 'default'
    case 'SYS':
      return 'outline'
  }
}

export function getUserTypeLabel(user: User) {
  if (user.userType === 'SYS') {
    return 'Hệ thống'
  }
  return user.provider
}
