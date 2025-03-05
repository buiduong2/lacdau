import { Authority } from '@/types/auth/resTypes'
import * as z from 'zod'

export const schema = z.object({
  userId: z.number(),
  permission: z.object({
    ...Object.fromEntries(
      Object.values(Authority).map((au) => [
        au,
        z
          .boolean()
          .optional()
          .transform((value) => Boolean(value)),
      ]),
    ),
  }),
})

export type TypeSchema = z.infer<typeof schema>
