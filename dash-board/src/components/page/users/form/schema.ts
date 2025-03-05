import * as z from 'zod'

export const schema = z
  .object({
    displayName: z.string().max(100).nonempty(),
    email: z.string().email().nonempty(),
    username: z.string().max(100).nonempty(),
    password: z.string().max(100).nonempty(),
    confirmPassword: z.string().max(100).nonempty(),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: "Passwords don't match",
    path: ['confirmPassword'], // path of error
  })

export type Schema = z.infer<typeof schema>
