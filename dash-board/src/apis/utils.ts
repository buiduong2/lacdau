import type { ValidationError } from '@/types'
import { AxiosService } from '@/utils/axios-service'
import { AxiosError } from 'axios'

export async function fetchResouceSimple<R>(uri: string): Promise<R> {
  const res = await getResourceClient().get(uri)
  return res.data
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

  try {
    const res = await getResourceClient()(`/${uri}`, { method, data: formData })
    return res.data
  } catch (error) {
    if (error instanceof AxiosError) {
      if (error.status === 400) {
        throw error.response?.data as ValidationError
      }
    }
  }

  throw new Error('Error')
}

export async function fetchChangeSimple<I, R>(
  uri: string,
  body: unknown,
  method: 'PUT' | 'POST',
): Promise<R> {
  try {
    const res = await getResourceClient()(uri, { method, data: body })
    return res.data
  } catch (error) {
    if (error instanceof AxiosError) {
      if (error.status === 400) {
        throw error.response?.data as ValidationError
      }
    }
  }

  throw new Error('Error')
}

export async function fetchDeleteSimple(uri: string, id: any) {
  await getResourceClient().delete<void>(`/${uri}/${id}`)
}

export async function fetchDeleteMulti(uri: string, ids: string[] | number[]) {
  return getResourceClient().delete(uri, {
    params: { id: ids },
    paramsSerializer: {
      indexes: null,
    },
  })
}

export function getAuthClient(): AxiosService['authClient'] {
  return AxiosService.getInstance().getAuthClient()
}

export function getResourceClient(): AxiosService['resourceClient'] {
  return AxiosService.getInstance().getResourceClient()
}
