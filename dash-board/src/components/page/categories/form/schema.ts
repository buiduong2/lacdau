import * as z from 'zod'

export const schema = z.object({
  name: z.string().min(1).max(50),
  parentId: z.number().int().optional(),
  image: z.any().optional(),
})

export type TypeSchema = z.infer<typeof schema>
