import type { Brand } from '@/types/brands/resTypes'
import { acceptHMRUpdate, defineStore } from 'pinia'

export const useBrandStore = defineStore('brand-store', () => {
  const brands = ref<Brand[]>([])
  let isInited = false

  async function fetchInit() {
    if (!isInited) {
      const data = await fetchBrands()

      brands.value.push(...data)
      isInited = true
    }
    return
  }

  function setDirty() {
    isInited = false
  }

  function remove(id: number) {
    brands.value = brands.value.filter((b) => b.id !== id)
    setDirty()
  }

  return {
    brands,
    fetchInit,
    setDirty,
    remove,
  }
})

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useBrandStore, import.meta.hot))
}
