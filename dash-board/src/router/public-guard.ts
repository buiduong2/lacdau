import type { RouteLocationRaw } from 'vue-router'
import type { Guard } from '.'

const publicPaths: RouteLocationRaw[] = ['/403']
export const publicGuard: Guard = (to) => {
  if (publicPaths.includes(to.path)) {
    return true
  }
}
