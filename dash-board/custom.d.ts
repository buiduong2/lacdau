import type { Authority } from '@/types/auth/resTypes'
import 'vue-router'

declare module 'vue-router' {
  interface RouteMeta {
    authority?: Authority
    breadcrumb: string
  }
}
