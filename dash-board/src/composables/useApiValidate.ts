import { type FormContext } from 'vee-validate'

export type Option<T extends FormContext<any>> = {
  name: keyof T['values'] extends string ? keyof T['values'] : never
  form: T
  validator: (value: string) => Promise<{ data: boolean }>
  debounceTime?: number
}

export function useApiValidate<T extends FormContext<any>>(option: Option<T>) {
  const { name, form, validator, debounceTime = 1000 } = option

  const isValidating = ref<boolean>(false)
  const error = ref<boolean>()
  let isStop = false
  let validatedValue: any = null
  const validatorDebounced = debounce(debounceTime, async (str) => {
    if (isStop) {
      return
    }
    validatedValue = str
    isValidating.value = true
    const res = await validator(str)
    isValidating.value = false
    error.value = res.data
  })
  const handle = watch([form.values], () => {
    if (form.isFieldDirty(name) && form.isFieldValid(name) && !isBlank(form.values[name])) {
      if (validatedValue === form.values[name]) {
        return
      }
      isStop = false
      validatorDebounced(form.values[name])
    } else {
      isStop = true
      error.value = undefined
      isValidating.value = false
    }
  })
  return {
    stop: () => handle.stop(),
    error,
    isValidating,
  }
}
