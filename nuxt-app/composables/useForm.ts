import { z } from 'zod'
export function useForm<T extends z.ZodRawShape>(schema: z.ZodObject<T>) {
	const value = ref<any>({})
	const errors = ref<Record<string, string | undefined>>({})
	const isSubmiting: { value: boolean } = { value: false }
	const isDirty: { value: boolean } = { value: false }

	for (const key in schema.shape) {
		provide<any>(key, {
			shape: schema.shape[key],
			value: value,
			setValue: (newVal: any) => (value.value[key] = newVal),
			validate: () => {
				const result = schema.shape[key].safeParse(value.value[key])
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

	function submit(cb: (value: z.infer<typeof schema>) => void) {
		return (e: Event) => {
			if (isSubmiting.value) return
			isDirty.value = true
			const result = schema.safeParse(value.value)
			if (result.success) {
				cb(result.data)
			} else {
				const fieldErrors = result.error.flatten().fieldErrors

				if (fieldErrors) {
					for (const key in fieldErrors) {
						errors.value[key] = (fieldErrors as any)[key][0]
					}
				}
			}
		}
	}
	return { submit }
}
