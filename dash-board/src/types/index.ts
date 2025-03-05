import type { ParamValue, RouteRecordInfo } from 'vue-router'
import type { RouteNamedMap } from 'vue-router/auto-routes'

export interface PageResponse<T> {
  page: Page
  content: T[]
}

export interface Page {
  size: number
  number: number
  totalElements: number
  totalPages: number
}

export interface ValidationError {
  status: number
  error: string
  message: string
  timestmap: string
  errors: {
    field: string
    message: string
  }[]
}

export interface ImageDTO {
  id: number
  src: string
  alt: string
  position: number
}

export type IsEqual<T, U> =
  (<G>() => G extends T ? 1 : 2) extends <G>() => G extends U ? 1 : 2 ? true : false
export type Assert<T extends true> = T

export type RouteEditable<T extends keyof RouteNamedMap = keyof RouteNamedMap> =
  T extends keyof RouteNamedMap
    ? RouteNamedMap[T] extends RouteRecordInfo<
        string,
        string,
        { id: ParamValue<true> },
        { id: ParamValue<false> }
      >
      ? T
      : never
    : never

export type RouteCreateable<T extends keyof RouteNamedMap = keyof RouteNamedMap> =
  T extends keyof RouteNamedMap ? (T extends `${string}/create` ? T : never) : never
