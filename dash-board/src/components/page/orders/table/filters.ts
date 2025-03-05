import { OrderStage, OrderType, PaymentStatus } from '@/types/orders/resType'
import type { TableFacetedFilterInput, TableFacetedFilterPick } from '../../app/table/types'

export async function getFilters() {
  const filterInputs: TableFacetedFilterInput[] = [
    {
      name: 'id',
      placeholder: 'ID Đơn hàng',
      label: 'ID ĐH',
    },
    {
      name: 'customerId',
      placeholder: 'ID khách hàng',
      label: 'ID KH',
    },
  ]

  const filterPicks: TableFacetedFilterPick[] = [
    {
      columnId: 'paymentStatus',
      options: Object.keys(PaymentStatus).map((key) => ({
        label: key,
        value: key,
      })),
      title: 'TT thanh toán',
    },
    {
      columnId: 'stage',
      options: Object.keys(OrderStage).map((key) => ({
        label: key,
        value: key,
      })),
      title: 'TT Đơn hàng',
    },
    {
      columnId: 'type',
      options: Object.keys(OrderType).map((key) => ({
        label: key,
        value: key,
      })),
      title: 'Loại đơn hàng',
    },
  ]

  return { filterInputs, filterPicks }
}
