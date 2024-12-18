//Định nghĩa về các coulmn của Table
import DataTableColumn from '@/components/page/app/table/AppDataTableColumn.vue'
import type { BadgeVariants } from '@/components/ui/badge'
import Badge from '@/components/ui/badge/Badge.vue'
import type { Product, ProductStatus } from '@/types/products/resTypes'
import type { ColumnDef } from '@tanstack/vue-table'

export const columns: ColumnDef<Product>[] = [
  {
    accessorKey: 'id',
    header: ({ column }) => <DataTableColumn column={column} title="ID" />,
    cell: ({ row }) => <div class="text-left">{row.getValue('id')}</div>,
  },
  {
    accessorKey: 'mainImage',
    header: ({ column }) => <DataTableColumn column={column} title="Hình ảnh" />,
    cell: ({ row }) => (
      <img class="size-20 rounded-lg" src={row.original.mainImage?.src || '/placeholder.svg'} />
    ),
  },
  {
    accessorKey: 'name',
    header: ({ column }) => <DataTableColumn column={column} title="Tên sản phẩm" />,
    cell: ({ row }) => <div class="text-left">{row.getValue('name')}</div>,
  },
  {
    accessorKey: 'originalPrice',
    header: ({ column }) => (
      <DataTableColumn class="justify-center" column={column} title="Giá gốc" />
    ),
    cell: ({ row }) => (
      <div class="font-medium text-center">{formartCurrency(row.getValue('originalPrice'))}</div>
    ),
  },
  {
    accessorKey: 'salePrice',
    header: ({ column }) => (
      <DataTableColumn class="justify-center" column={column} title="Giá khuyến mại" />
    ),
    cell: ({ row }) => (
      <div class="font-medium text-center">{formartCurrency(row.getValue('salePrice'))}</div>
    ),
  },
  {
    accessorKey: 'quantity',
    header: ({ column }) => (
      <DataTableColumn class="justify-center" column={column} title="Số lượng" />
    ),
    cell: ({ row }) => {
      const quantity = row.original.quantity
      return (
        <div class="font-medium text-center">
          {quantity > 0 ? quantity : <span class="text-red-500">Hết hàng</span>}
        </div>
      )
    },
  },
  {
    accessorKey: 'status',
    header: ({ column }) => (
      <DataTableColumn class="justify-center" column={column} title="Trạng thái" />
    ),
    cell: ({ row }) => (
      <Badge variant={getBadageVariant(row.original.status)} class="font-medium text-center">
        {row.original.status}
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
  { id: 'categoryIds' },
  { id: 'brandIds' },
  { id: 'attributeValueIds' },
  { id: 'search' },
]

function getBadageVariant(status: ProductStatus): BadgeVariants['variant'] {
  switch (status) {
    case 'ACTIVE':
      return 'secondary'
    case 'DRAFT':
      return 'outline'
    case 'ARCHIVED':
      return 'destructive'
    default:
      break
  }
}
