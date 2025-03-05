import { PaymentMethod } from './resType'

export interface OrderAddressUpdate {
  description: string
  payload: {
    fullName: string
    phone: string
    districtId: number
    detail: string
    message?: string
  }
}

export interface OrderItemUpdate {
  description: string
  payload: {
    orderItems: { productId: string; quantity: number }[]
  }
}

const methods: string[] = Object.values(PaymentMethod)

export interface OrderPaymentUpdate {
  description: string
  payload: {
    method: (typeof methods)[number]
    shippingPrice: number
  }
}

export interface OrderPaymentPaidUpdate {
  description: string
  payload: {
    amountPaid: number
  }
}

export interface OrderShipmentUpdate {
  description: string
  payload: {
    estimatedDelivery: Date
    shippingPrice: number
    trackingDetail: string
    carrier: string
  }
}
