import * as z from 'zod'

export const schema = z
  .object({
    password: z.string().max(100).min(8),
    confirmPassword: z.string().max(100).min(8),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: "Passwords don't match",
    path: ['confirmPassword'], // path of error
  })

export type Schema = z.infer<typeof schema>
