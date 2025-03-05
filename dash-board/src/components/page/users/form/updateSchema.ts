import * as z from 'zod'

export const schema = z.object({
  displayName: z.string().max(100).nonempty(),
  email: z
    .string()
    .email()
    .transform((str) => (str.length ? str : undefined))
    .optional(),
})

export type Schema = z.infer<typeof schema>
