export interface CustomerBasic {
  firstName: string
  lastName: string
  email: string
  totalOrder: number
  totalPaymentAmountPaid: number
  addressProvince: string
  lastOrder: string
  createdAt: string
  updatedAt: string
}

export interface Customer {
  id: number
  firstName: string
  lastName: string

  address: {
    id: number
    province: string
    district: string
    detail: string
  }
  status: 'ACTIVE' | 'INACTIVE'
  gender: 'MALE' | 'FEMALE'
  dob: string
  phone: string
  email: string
  userId: number
  createdAt: string
  updatedAt: string
}
