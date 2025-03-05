import type { UserPermission } from '@/types/roles/resTypes'
import { acceptHMRUpdate, defineStore } from 'pinia'

export const useRoleStore = defineStore('role-store', () => {
  const userPermissions = ref<UserPermission[]>([])
  let isInited = false

  async function fetchInit() {
    if (!isInited) {
      const { data } = await fetchUserPermissions()
      userPermissions.value = data
      isInited = true
    }
  }

  function setDirty() {
    isInited = false
  }

  return {
    userPermissions,
    fetchInit,
    setDirty,
  }
})

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useRoleStore, import.meta.hot))
}
