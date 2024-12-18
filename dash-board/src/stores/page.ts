import { acceptHMRUpdate, defineStore } from 'pinia'

export const usePageStore = defineStore('page-store', () => {
  // Khai báo một Global Reactive Data
  const pageData = ref({ title: '' })

  return {
    // return nó để sử dụng
    pageData,
  }
})

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(usePageStore, import.meta.hot))
}
