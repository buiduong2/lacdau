import type { PageResponse } from '@/types'
import type {
  OrderAddressUpdate,
  OrderItemUpdate,
  OrderPaymentPaidUpdate,
  OrderPaymentUpdate,
  OrderShipmentUpdate,
} from '@/types/orders/reqType'
import type { Order, OrderBasic, OrderLog, OrderProcess, OrderStage } from '@/types/orders/resType'

export function fetchOrder(id: number | string) {
  return getResourceClient().get<Order>('/api/admin/orders/' + id)
}

export function fetchOrders() {
  return getResourceClient().get<PageResponse<OrderBasic>>(
    '/api/admin/orders' + window.location.search.toString(),
  )
}

export function fetchOrderProcess(id: number | string, stage: OrderStage, message: string) {
  return getResourceClient().post<OrderProcess>('/api/admin/orders/process/' + id, {
    nextStage: stage,
    description: message,
  })
}

export function fetchOrderUpdateAddress(id: number | string, action: OrderAddressUpdate) {
  return getResourceClient().put<OrderProcess>(
    '/api/admin/orders/process/' + id + '/order-address',
    action,
  )
}

export function fetchOrderUpdateItem(id: number | string, action: OrderItemUpdate) {
  return getResourceClient().put<OrderProcess>(
    '/api/admin/orders/process/ ' + id + '/order-items',
    action,
  )
}

export function fetchOrderUpdatePayment(id: number | string, action: OrderPaymentUpdate) {
  return getResourceClient().put<OrderProcess>(
    '/api/admin/orders/process/' + id + '/payment',
    action,
  )
}

export function fetchOrderUpdatePaymentPaid(id: number | string, action: OrderPaymentPaidUpdate) {
  return getResourceClient().put<OrderProcess>(
    '/api/admin/orders/process/' + id + '/payment/amount-paid',
    action,
  )
}

export function fetchOrderCreateShipment(id: number | string, action: OrderShipmentUpdate) {
  return getResourceClient().post<OrderProcess>(
    '/api/admin/orders/process/' + id + '/shipment',
    action,
  )
}

export function fetchOrderUpdateShipment(id: number | string, action: OrderShipmentUpdate) {
  return getResourceClient().put<OrderProcess>(
    '/api/admin/orders/process/' + id + '/shipment',
    action,
  )
}

export function fetchOrderLogs(id: number | string) {
  return getResourceClient().get<OrderLog[]>('/api/admin/orders/' + id + '/log')
}

export function fetchOrderUpdatePaymentRefund(id: number | string, action: OrderPaymentPaidUpdate) {
  return getResourceClient().put<OrderProcess>(
    '/api/admin/orders/process/' + id + '/payment/refund',
    action,
  )
}
