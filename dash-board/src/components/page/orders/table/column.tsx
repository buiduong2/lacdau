import DataTableColumn from '@/components/page/app/table/AppDataTableColumn.vue'
import type { BadgeVariants } from '@/components/ui/badge'
import Badge from '@/components/ui/badge/Badge.vue'
import { OrderStage, OrderType, PaymentStatus, type OrderBasic } from '@/types/orders/resType'
import type { ColumnDef } from '@tanstack/vue-table'

export const columns: ColumnDef<OrderBasic>[] = [
  {
    accessorKey: 'id',
    header: ({ column }) => <DataTableColumn column={column} title="ID" />,
    cell: ({ row }) => <div class="text-left">#{row.original.id}</div>,
  },
  {
    accessorKey: 'customerId',
    header: ({ column }) => <DataTableColumn column={column} title="ID khách hàng" />,
    cell: ({ row }) => <div class="text-center">{row.original.customer.id}</div>,
  },
  {
    accessorKey: 'stage',
    header: ({ column }) => <DataTableColumn column={column} title="Trạng thái" />,
    cell: ({ row }) => (
      <Badge variant={getStageVariant(row.original.stage)} class="font-medium text-center">
        {row.original.stage}
      </Badge>
    ),
  },
  {
    accessorKey: 'type',
    header: ({ column }) => (
      <DataTableColumn class="justify-center" column={column} title="Loại đơn hàng" />
    ),
    cell: ({ row }) => (
      <Badge variant={getTypeVariant(row.original.type)} class="font-medium text-center">
        {row.original.type}
      </Badge>
    ),
  },
  {
    accessorKey: 'paymentTotalItem',
    header: ({ column }) => (
      <DataTableColumn class="justify-center" column={column} title="SL mật hàng" />
    ),
    cell: ({ row }) => <div class="font-medium text-center">{row.original.payment.totalItem}</div>,
  },
  {
    accessorKey: 'totalPaymentAmountPaid',
    header: ({ column }) => (
      <DataTableColumn class="justify-center" column={column} title="Tổng giá trị" />
    ),
    cell: ({ row }) => (
      <div class="font-medium text-center">
        {formartCurrency(row.original.payment.totalPrice || row.original.payment.subTotalPrice)}
      </div>
    ),
  },
  {
    accessorKey: 'paymentStatus',
    header: ({ column }) => (
      <DataTableColumn class="justify-center" column={column} title="Trạng thái thanh toán" />
    ),
    cell: ({ row }) => (
      <Badge
        variant={getPaymentStatusVariant(row.original.payment.status)}
        class="font-medium text-center"
      >
        {row.original.payment.status}
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
]

function getStageVariant(stage: OrderStage): BadgeVariants['variant'] {
  switch (stage) {
    case 'PENDING':
    case 'FAILURE':
      return 'default'
    case OrderStage.CANCELED:
      return 'destructive'
    case OrderStage.COMPLETED:
      return 'secondary'
    default:
      return 'default'
  }
}

function getTypeVariant(type: OrderType): BadgeVariants['variant'] {
  switch (type) {
    case OrderType.IN_STORE:
      return 'outline'
    default:
      return 'default'
  }
}

function getPaymentStatusVariant(status: PaymentStatus): BadgeVariants['variant'] {
  switch (status) {
    case PaymentStatus.PENDING:
    case PaymentStatus.PROCESSING:
      return 'default'

    case PaymentStatus.PAID:
      return 'outline'
    default:
      return 'destructive'
  }
}
