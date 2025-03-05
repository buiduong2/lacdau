import { string } from 'zod'

export interface OrderBasic {
  id: number
  stage: OrderStage
  type: OrderType
  createdAt: string
  updatedAt: string
  payment: {
    totalItem: number
    amountPaid: number | null
    subTotalPrice: number
    shippingPrice: number | null
    totalPrice: number | null
    status: PaymentStatus
  }
  customer: {
    id: number
    firstName: string
    lastName: string
  }
}

export interface Order {
  id: number
  type: OrderType
  stage: OrderStage
  customer: {
    id: number
    firstName: string
    lastName: string
    gender: 'MALE' | 'FEMALE'
    phone: string
    email: string
    createdAt: string
    updatedAt: string
  }
  payment: {
    id: number
    method: PaymentMethod
    amountPaid: number | null
    subTotalPrice: number
    shippingPrice: number | null
    totalPrice: number | null
    status: PaymentStatus
    paymentDate: string | null
    createdAt: string
    updatedAt: string
  }
  shipment: {
    id: number
    estimatedDelivery: string
    shippingPrice: number
    trackingDetail: string
    carrier: string
    createdAt: string
    updatedAt: string
  } | null
  orderAddress: {
    id: number
    fullName: string
    phone: string
    provinceId: number
    districtId: number
    province: string
    district: string
    detail: string
    message: string
    createdAt: string
    updatedAt: string
  } | null
  orderItems: {
    id: number

    product: {
      id: number
      productCode: string
      name: string
      mainImage: {
        src: string
        alt: string
      } | null
    }
    requestedQuantity: number
    suppliedQuantity: number
    unitPrice: number
    salePrice: number | null
    status: OrderItemStatus
  }[]
}

export interface OrderProcess {
  orderId: number
  stage: OrderStage
  type: OrderType
  customerId: number
  paymentId: number
  shipmentId: number | null
  orderAddressId: number
}

export interface OrderLog {
  id: number
  orderId: number
  employee: {
    id: number
    firstName: string
    lastName: string
  }
  stage: OrderStage
  action: OrderStageAction | null
  description: string
  createdAt: string
  updatedAt: string
}

export enum OrderStage {
  PENDING = 'PENDING',
  CONFIRMATION = 'CONFIRMATION',
  PAYMENT = 'PAYMENT',
  PACKING = 'PACKING',
  SHIPPING = 'SHIPPING',
  DELIVERED = 'DELIVERED',
  SUCCESSFUL = 'SUCCESSFUL',
  COMPLETED = 'COMPLETED',
  FAILURE = 'FAILURE',
  CANCELED = 'CANCELED',
}
export enum OrderType {
  ONLINE = 'ONLINE',
  IN_STORE = 'IN_STORE',
}
export enum PaymentStatus {
  PROCESSING = 'PROCESSING',
  PENDING = 'PENDING',
  PAID = 'PAID',
  REFUNDED = 'REFUNDED',
}
export enum PaymentMethod {
  COD = 'COD',
  CARD = 'CARD',
  CASH = 'CASH',
}
export enum OrderItemStatus {
  FULLY_SUPPLIED = 'FULLY_SUPPLIED',
  PARTIALLY_SUPPLIED = 'PARTIALLY_SUPPLIED',
  NOT_SUPPLIED = 'NOT_SUPPLIED',
}

export enum OrderStageAction {
  UPDATE_ADDRESS = 'UPDATE_ADDRESS',
  UPDATE_ORDER_ITEM = 'UPDATE_ORDER_ITEM',
  UPDATE_PAYMENT = 'UPDATE_PAYMENT',
  UPDATE_PAYMENT_AMOUNT_PAID = 'UPDATE_PAYMENT_AMOUNT_PAID',
  UPDATE_PAYMENT_REFUND = 'UPDATE_PAYMENT_REFUND',
  CREATE_SHIPMENT = 'CREATE_SHIPMENT',
  UPDATE_SHIPMENT = 'UPDATE_SHIPMENT',
}
