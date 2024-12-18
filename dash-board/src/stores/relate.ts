import type { RelateGroup } from '@/types/relate-groups/resTypes'
import { acceptHMRUpdate, defineStore } from 'pinia'

export const useRelateStore = defineStore('relate-store', () => {
  const relateGroups = ref<RelateGroup[]>([])
  let isInited = false

  async function fetchInit() {
    if (!isInited) {
      const data = await fetchRelateGroups()
      relateGroups.value = data
      isInited = true
    }
    return
  }

  function setDirty() {
    isInited = false
  }

  async function fetchDetail(id: number) {
    return fetchRelateGroupInfo(id)
  }

  return {
    relateGroups,
    fetchInit,
    fetchDetail,
    setDirty,
  }
})

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useRelateStore, import.meta.hot))
}
