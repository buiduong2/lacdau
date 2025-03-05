import type { Authority } from '@/types/auth/resTypes'
import {
  createRouter,
  createWebHistory,
  type RouteLocationNormalized,
  type RouteLocationNormalizedLoaded,
  type RouteLocationRaw,
} from 'vue-router'
import { routes } from 'vue-router/auto-routes'
import { authenticationGuard } from './authentication-guard'
import { publicGuard } from './public-guard'
import { authorizeGuard } from './authorize-guard'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

// Return String if navigate
// return true if handle next
// return false if denined

type Meta = {
  isAuthenticated: boolean
  authorities: Authority[]
  setRouteToGo: (url: string) => void
}
export type Guard = (
  to: RouteLocationNormalized,
  from: RouteLocationNormalizedLoaded,
  meta: Meta,
) => RouteLocationRaw | undefined | true

const guards = [publicGuard, authenticationGuard, authorizeGuard]
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()

  const meta: Meta = {
    isAuthenticated: authStore.isAuthenticated,
    authorities: authStore.authorities,
    setRouteToGo: (url: string) => (authStore.routeToGo.value = url),
  }

  for (let i = 0; i < guards.length; i++) {
    const guard = guards[i]
    const navigate = guard(to, from, meta)
    if (typeof navigate === 'string') {
      return next(navigate)
    } else if (navigate === true) {
      return next()
    }
  }

  return next()
})

export default router
