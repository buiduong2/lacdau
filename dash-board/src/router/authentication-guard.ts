import type { RouteLocationRaw } from 'vue-router'
import type { Guard } from '.'

const guestPaths: RouteLocationRaw[] = ['/login', '/callback']
export const authenticationGuard: Guard = (to, from, { isAuthenticated, setRouteToGo }) => {
  if (isAuthenticated) {
    if (guestPaths.includes(to.path)) {
      return '/'
    }
    return
  } else {
    if (guestPaths.includes(to.path)) {
      return true
    } else {
      setRouteToGo(to.path)
      return '/login'
    }
  }
}
