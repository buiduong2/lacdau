import type { AttributeValue } from '@/types/attribute-values/resTypes'
import type { Attribute } from '@/types/attributes/resTypes'
import { acceptHMRUpdate, defineStore } from 'pinia'

export const useAttributeStore = defineStore('attribute-store', () => {
  const attributeValues = ref<AttributeValue[]>([])
  const attributes = ref<Attribute[]>([])
  let isInited = false

  async function fetchInit() {
    if (!isInited) {
      const data = await Promise.all([fetchAttributeValues(), fetchAttributes()])
      attributeValues.value = data[0]
      attributes.value = data[1]
      isInited = true
    }
    return
  }

  function getAttrValGroupByAttrId() {
    return groupAttrValByAttrId(attributeValues.value)
  }

  function getAttributeValuesByIdIn(ids?: number[]): AttributeValue[] {
    if (!ids) return []
    return attributeValues.value.filter((attrVal) => ids.includes(attrVal.id))
  }

  function groupAttrValByAttrId(attributeValue: AttributeValue[]) {
    const res: { [id: number | string]: AttributeValue[] } = {}
    attributeValue.forEach((attrVal) => {
      const attrId = attrVal.attributeId
      if (!res[attrId]) {
        res[attrId] = []
      }
      res[attrId].push(attrVal)
    })

    return res
  }

  function removeAttribute(id: number) {
    attributes.value = attributes.value.filter((a) => a.id !== id)
    setDirty()
  }

  function removeAttributeValue(id: number) {
    setDirty()
    attributeValues.value = attributeValues.value.filter((av) => av.id !== id)
  }

  function setDirty() {
    isInited = false
  }

  return {
    attributeValues,
    attributes,
    fetchInit,
    getAttrValGroupByAttrId,
    groupAttrValByAttrId,
    getAttributeValuesByIdIn,
    setDirty,
    removeAttribute,
    removeAttributeValue,
  }
})

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useAttributeStore, import.meta.hot))
}
