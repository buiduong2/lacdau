import type { Guard } from '.'

export const authorizeGuard: Guard = (to, from, { authorities }) => {
  if (to.meta.authority && !authorities.includes(to.meta.authority)) {
    return '/403'
  }
}
