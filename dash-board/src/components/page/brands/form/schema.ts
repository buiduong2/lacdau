import type { BrandBasic } from '@/types/brands/resTypes'
import * as z from 'zod'
export function getSchema(brand?: BrandBasic) {
  return z.object({
    name: z
      .string()
      .min(1)
      .max(50)
      .default(brand?.name || ''),
  })
}
