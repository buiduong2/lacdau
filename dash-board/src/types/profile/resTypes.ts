export interface EmployeeProfile {
  id: number
  firstName: string
  lastName: string
  status: 'ACTIVE' | 'INACTIVE'
  gender: 'MALE' | 'FEMALE'
  dob: string
  phone: string
  email: string
  userId: number
  createdAt: string
  updatedAt: string
  address: {
    id: number
    provinceId: number
    districtId: number
    province: string
    district: string
    detail: string
  }
}
