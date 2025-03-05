import type { EmployeeRegister } from '@/types/profile/reqTypes'
import type { EmployeeProfile } from '@/types/profile/resTypes'

export async function fetchEmployeeCheck() {
  return getResourceClient().get<boolean>('/api/employees/check-register')
}

export async function fetchEmployeeProfileCreate(body: EmployeeRegister) {
  return getResourceClient().post(`api/employees`, body)
}

export async function fetchEmployeeProfile() {
  return getResourceClient().get<EmployeeProfile>('/api/employees')
}

export async function fetchEmployeeProfileEdit(body: EmployeeRegister) {
  return getResourceClient().put(`api/employees`, body)
}
