import { ProductStatus } from '@/types/products/resTypes'
import * as z from 'zod'

export const zodSchema = z.object({
  name: z.string(),
  originalPrice: z.number(),
  salePrice: z
    .string()
    .or(z.null())
    .or(z.number().int())
    .transform((value) => (typeof value === 'string' ? undefined : value))
    .optional(),
  quantity: z.number(),
  detail: z.object({
    specifications: z.array(z.string().min(1)).default(['']),
  }),
  prefixProductCode: z.string(),
  relateInfo: z
    .object({
      name: z.string(),
      relateGroupId: z.number().int(),
    })
    .or(z.null())
    .optional(),
  attributeValueIds: z
    .array(z.number().int())
    .or(z.null())
    .transform((z) => (z?.length ? z : undefined))
    .optional(),
  categoryId: z.number().or(z.null()).optional(),
  brandId: z.number().or(z.null()).optional(),
  status: z.enum(['ACTIVE', 'DRAFT', 'ARCHIVED']).transform((s) => ProductStatus[s]),
  imageData: z
    .object({
      thumbnails: z
        .array(
          z.object({
            id: z.number().int().optional(),
            position: z.number().int().optional(),
          }),
        )
        .transform((arr) => {
          return arr ? arr.map((a, index) => ({ ...a, position: index + 1 })) : arr
        }),
    })
    .optional(),
  mainImage: z.any().optional(),
  thumbnails: z.array(z.any()).optional(),
})

export type TypeSchema = z.infer<typeof zodSchema>
