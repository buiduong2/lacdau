import * as z from 'zod'
export const schema = z.object({
  name: z.string().min(1).max(255),
  attributeValues: z
    .array(z.object({ name: z.string().min(1).max(255) }))
    .optional()
    .default([{ name: '' }]),
})
