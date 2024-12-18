import * as z from 'zod'
export const schema = z.object({
  name: z.string().min(1).max(255),
  attributeValues: z
    .array(z.object({ name: z.string().min(1).max(255) }))
    .min(1)
    .optional()
    .default([{ name: '' }]),
  removeAttributeValueId: z.array(z.number()).optional(),
})
