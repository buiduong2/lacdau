export interface EmployeeRegister {
  firstName: string
  lastName: string
  email: string
  phone: string
  gender: 'MALE' | 'FEMALE'
  dob: Date
  address: {
    districtId: number
    detail: string
  }
}
