export function useField(name: string) {
	const injteted = inject<Injected>(name)

	if (!injteted) {
		throw new Error('Must be define name' + name)
	}
	const {
		value: formValue,
		setValue: setFieldvalue,
		error: formError,
		validate,
		isDirty: isFormDirty
	} = injteted

	let isDirty = false
	const error = computed<string>(() => formError.value[name])
	const value = computed<any>(() => formValue.value[name])

	function setValue(newVal: any) {
		setFieldvalue(newVal)
		if (isDirty || isFormDirty.value) {
			validate()
		}
	}

	function onInput(e: Event) {
		let value: string | undefined
		value = (e.target as HTMLInputElement).value
		if (value.length === 0 || value.trim().length === 0) {
			value = undefined
		}
		setValue(value)
	}

	function onBlur() {
		isDirty = true
		validate()
	}

	return {
		value,
		setValue,
		error,
		onBlur,
		onInput
	}
}

type Injected = {
	value: Ref<Record<string, any>>
	setValue: (newVal: any) => void
	validate: () => void
	error: Ref<Record<string, any>>
	isDirty: { value: boolean }
}
