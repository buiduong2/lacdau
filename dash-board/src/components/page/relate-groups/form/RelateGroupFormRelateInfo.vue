<script setup lang="ts">
import type {
  ProductRelateInfo,
  RelateGroupCreate,
  RelateInfoUpdate,
} from '@/types/relate-groups/reqTypes'
import { useField, useFieldArray } from 'vee-validate'
type FieldName = keyof RelateGroupCreate

const props = defineProps<{ groupId?: number }>()

const fieldName: FieldName = 'relateInfos'

const { errorMessage } = useField(fieldName)

const { push, fields, remove } = useFieldArray<RelateInfoUpdate>(fieldName)

const productMap = new Map<string, ProductRelateInfo>()

if (props.groupId) {
  const data: ProductRelateInfo[] = await fetchProductRelateByGroupId(props.groupId)
  data.forEach((data) => {
    productMap.set(data.id, data)
    push({
      name: data.relateInfo?.name || '',
      position: fields.value.length,
      productId: data.id,
    })
  })
}

const products = computed<ProductRelateInfo[]>(() =>
  fields.value
    .filter((f) => productMap.has(f.value.productId))
    .map((f) => productMap.get(f.value.productId)!),
)

function onAddProduct(product: ProductRelateInfo) {
  productMap.set(product.id, product)
  push({
    name: product.relateInfo?.name || '',
    position: fields.value.length,
    productId: product.id,
  })
}

function onRemoveProduct(index: number) {
  productMap.delete(fields.value[index].value.productId)
  remove(index)
}
</script>
<template>
  <RelateGroupFormRelateInfoAdd
    @add-product="onAddProduct"
    :product-codes="fields.map((f) => f.value.productId)"
  />
  <h3 class="text-sm font-medium">Sản phảm sẽ thêm:</h3>
  <p class="text-sm text-destructive" v-if="errorMessage">{{ errorMessage }}</p>
  <div class="grid grid-cols-2 gap-4">
    <RelateGroupFormRelateInfoList
      :products="products"
      :field-name="fieldName"
      :group-id="groupId"
      @remove-product="onRemoveProduct"
    />
  </div>
</template>
<style scoped></style>
