import { z, ZodObject } from 'zod'
type Option<T> = {
	initialValues: DeepPartial<T>
}
export function useForm<T extends z.ZodRawShape>(
	schema: z.ZodObject<T> | z.ZodEffects<z.ZodObject<T>>,
	opt?: Option<z.infer<z.ZodObject<T>>>
) {
	const { initialValues = {} } = opt || {}
	const shape =
		schema instanceof ZodObject ? schema.shape : schema.innerType().shape

	const value = ref<any>(initialValues)
	const errors = ref<Record<string, string | undefined>>({})
	const isSubmiting: { value: boolean } = { value: false }
	const isDirty: { value: boolean } = { value: false }

	for (const key in shape) {
		provide<any>(key, {
			shape: shape[key],
			value: value,
			setValue: (newVal: any) => (value.value[key] = newVal),
			validate: () => {
				const result = shape[key].safeParse(value.value[key])
				if (result.error) {
					errors.value[key] = result.error.flatten().formErrors[0]
				} else {
					errors.value[key] = undefined
				}
			},
			error: errors,
			isDirty: isDirty
		})
	}

	async function submit(
		cb: (value: z.infer<typeof schema>) => Promise<void>
	) {
		if (isSubmiting.value) return
		try {
			isSubmiting.value = true
			isDirty.value = true
			const result = schema.safeParse(value.value)
			if (result.success) {
				await cb(result.data)
			} else {
				const fieldErrors = result.error.flatten().fieldErrors

				if (fieldErrors) {
					for (const key in fieldErrors) {
						errors.value[key] = (fieldErrors as any)[key][0]
					}
				}
			}
			isSubmiting.value = false
		} catch (error) {
			isSubmiting.value = false
			throw error
		}
	}

	return { submit, errors }
}
