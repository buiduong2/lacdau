import type { Profile } from '@/types/auth/resTypes'

export function fetchProfile() {
  return getAuthClient().get<Profile>('/api/profile')
}
