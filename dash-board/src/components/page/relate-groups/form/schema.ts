import type { RelateGroupCreate } from '@/types/relate-groups/reqTypes'
import * as z from 'zod'
export const schema = z.object({
  name: z.string(),
  relateInfos: z
    .array(
      z.object({
        productId: z.string(),
        name: z.string().min(1),
        position: z.number().int(),
      }),
    )
    .optional()
    .transform((ris) => ris?.map((ri, index) => ({ ...ri, position: index + 1 }))),
}) satisfies z.ZodType<RelateGroupCreate>
