<script setup lang="ts">
import type { Product } from '@/types/products/resTypes'
import type { ProductUpdate } from '@/types/productsTypes'

const props = defineProps<{ product: Product }>()
const emit = defineEmits<{
  (e: 'closeExpanded'): void
  (e: 'editById', value: { id: string; product: ProductUpdate }): void
}>()

const clone = ref<Product>({
  ...props.product,
  salePrice: props.product.salePrice,
})

function editById(id: string) {
  let salePrice: number | null = clone.value.salePrice || null
  emit('editById', {
    id,
    product: {
      ...clone.value,
      salePrice,
    },
  })

  emit('closeExpanded')
}
</script>
<template>
  <TableRow class="bg-secondary">
    <TableCell colspan="2">
      {{ product.id }}
    </TableCell>
    <TableCell>
      <img class="size-20 rounded-lg" :src="clone.mainImage?.src || '/placeholder.svg'" />
    </TableCell>
    <TableCell class="py-1">
      <Textarea class="min-h-20 block" v-model="clone.name"></Textarea>
    </TableCell>
    <TableCell class="py-1">
      <Input class="min-h-20 block text-center" type="number" v-model="clone.originalPrice"></Input>
    </TableCell>
    <TableCell class="py-1">
      <Input
        class="min-h-20 block text-center"
        :value="clone.salePrice"
        type="number"
        @update:model-value="(value) => (clone.salePrice = value === 0 ? null : Number(value))"
      />
    </TableCell>
    <TableCell class="py-1">
      <Input class="min-h-20 block text-center" type="number" v-model="clone.quantity" />
    </TableCell>
    <TableCell class="text-center" colspan="100">
      <div class="flex gap-4 justify-center">
        <Button class="flex-grow" variant="outline" @click="editById(clone.id)">Lưu</Button>
        <Button class="flex-grow" variant="destructive" @click="$emit('closeExpanded')"
          >Hủy bỏ</Button
        >
      </div>
    </TableCell>
  </TableRow>
</template>

<style scoped></style>
