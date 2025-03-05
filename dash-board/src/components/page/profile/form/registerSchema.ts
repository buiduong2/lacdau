import { z } from 'zod'
const now = new Date()
now.setFullYear(now.getFullYear() - 1)
export const schema = z.object({
  firstName: z.string().min(1),
  lastName: z.string().min(1),
  email: z.string().email().min(1),
  phone: z.string().min(1),
  gender: z.enum(['MALE', 'FEMALE']),
  dob: z.date().max(now, { message: 'Bạn quá trẻ' }),
  address: z.object({
    provinceId: z.string().refine((n) => Number(n)),
    districtId: z.string().refine((n) => Number(n)),
    detail: z.string().min(1),
  }),
})
