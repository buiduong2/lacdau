import DataTableColumn from '@/components/page/app/table/AppDataTableColumn.vue'
import type { CustomerBasic } from '@/types/customers/resType'
import type { ColumnDef } from '@tanstack/vue-table'

export const columns: ColumnDef<CustomerBasic>[] = [
  {
    accessorKey: 'id',
    header: ({ column }) => <DataTableColumn column={column} title="ID" />,
    cell: ({ row }) => <div class="text-left">{row.getValue('id')}</div>,
  },
  {
    accessorKey: 'name',
    header: ({ column }) => <DataTableColumn column={column} title="Tên khách hàng" />,
    cell: ({ row }) => (
      <div class="text-left">{row.original.firstName + ' ' + row.original.lastName}</div>
    ),
  },
  {
    accessorKey: 'email',
    header: ({ column }) => (
      <DataTableColumn class="justify-center" column={column} title="Email" />
    ),
    cell: ({ row }) => <div class="font-medium text-center">{row.original.email}</div>,
  },
  {
    accessorKey: 'totalOrder',
    header: ({ column }) => (
      <DataTableColumn class="justify-center" column={column} title="SL đơn hàng" />
    ),
    cell: ({ row }) => <div class="font-medium text-center">{row.original.totalOrder}</div>,
  },
  {
    accessorKey: 'totalPaymentAmountPaid',
    header: ({ column }) => (
      <DataTableColumn class="justify-center" column={column} title="SL tiền đã trả" />
    ),
    cell: ({ row }) => (
      <div class="font-medium text-center">{row.original.totalPaymentAmountPaid}</div>
    ),
  },

  {
    accessorKey: 'addressProvince',
    header: ({ column }) => (
      <DataTableColumn class="justify-center" column={column} title="Địa chỉ" />
    ),
    cell: ({ row }) => <div class="font-medium text-center">{row.original.addressProvince}</div>,
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
