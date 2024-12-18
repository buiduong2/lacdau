import type { ValidationError } from '@/types'

export const resourceURL = import.meta.env.VITE_RESOURCE_URL

export async function fetchResouceSimple<R>(uri: string): Promise<R> {
  const res = await fetch(`${resourceURL}${uri}`)

  if (res.ok) {
    return (await res.json()) as R
  }

  throw await res.json()
}

export interface FormDataItem<T = any> {
  key: string
  value: T
  type: T extends File ? 'IMAGE' : 'JSON'
}

export async function fetchChangeFormData<T>(
  uri: string,
  data: FormDataItem<any>[],
  method: 'POST' | 'PUT',
): Promise<T> {
  const formData = new FormData()

  data.forEach(({ key, value, type }) => {
    if (type === 'IMAGE') {
      if (Array.isArray(value)) {
        for (let i = 0; i < value.length; i++) {
          formData.append(key, value[i])
        }
      } else {
        formData.append(key, value)
      }
    } else if (type === 'JSON') {
      formData.append(key, new Blob([JSON.stringify(value)], { type: 'application/json' }))
    }
  })

  const res = await fetch(`${resourceURL}/${uri}`, {
    method: method,
    body: formData,
  })

  if (res.ok) {
    return (await res.json()) as T
  }
  if (res.status === 400) {
    const errorData = await res.json()
    throw errorData as ValidationError
  }

  throw new Error('Error')
}

export async function fetchChangeSimple<I, R>(
  uri: string,
  body: unknown,
  method: 'PUT' | 'POST',
): Promise<R> {
  const res = await fetch(`${resourceURL}/${uri}`, {
    method: method,
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(body),
  })

  if (res.ok) {
    return (await res.json()) as R
  }
  if (res.status === 400) {
    const errorData = await res.json()
    throw errorData as ValidationError
  }

  throw new Error('Error')
}

export async function fetchDeleteSimple(uri: string, id: any) {
  const res = await fetch(`${resourceURL}/${uri}/${id}`, { method: 'DELETE' })
  if (res.ok) {
    return
  } else {
    throw new Error('Error on Delete')
  }
}
