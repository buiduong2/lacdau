import type { ProductCode } from '@/types/products/resTypes'
import { acceptHMRUpdate, defineStore } from 'pinia'

export const useProductCodeStore = defineStore('product-code-store', () => {
  const codes = ref<ProductCode[]>([])
  let isInited = false

  async function fetchInit() {
    if (!isInited) {
      codes.value = await fetchProductCode()
    }
    return
  }

  return {
    codes,
    fetchInit,
  }
})

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useProductCodeStore, import.meta.hot))
}
