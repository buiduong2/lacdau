import type { ValidationError } from '@/types'
import type { FormContext } from 'vee-validate'

export function addErrorFromServer(form: FormContext<any>, errors: ValidationError) {
  const fieldsError: Record<string, string[]> = {}
  errors.errors.forEach(({ field, message }) => {
    if (field.startsWith('<cross-parameter>.')) {
      field = field.substring('<cross-parameter>.'.length)
    }
    if (!fieldsError[field]) {
      fieldsError[field] = []
    }
    fieldsError[field].push(message)
  })

  Object.entries(fieldsError).forEach(([key, messages]) => {
    form.setFieldError(key, messages)
  })
}
