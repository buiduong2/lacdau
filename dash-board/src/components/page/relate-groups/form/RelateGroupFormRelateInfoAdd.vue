<script setup lang="ts">
import type { ProductRelateInfo } from '@/types/relate-groups/reqTypes'
import { z } from 'zod'

interface Props {
  productCodes?: string[]
}
const props = withDefaults(defineProps<Props>(), {
  productCodes: () => [],
})

const emits = defineEmits<{
  (e: 'addProduct', payload: ProductRelateInfo): void
}>()

const schema = z
  .string()
  .refine((s) => s.trim().length > 0, 'Must not be empty')
  .refine((s) => !props.productCodes.includes(s), 'Product codes existsed')

const error = ref<string>()
const loading = ref(false)
const productCode = ref('')

async function submit() {
  const result = schema.safeParse(productCode.value)
  if (result.success) {
    try {
      loading.value = true
      const product = await fetchProductRelate(productCode.value)
      emits('addProduct', product)
      productCode.value = ''
    } catch (ex: any) {
      if ('status' in ex && 'message' in ex) {
        const status: number = ex.status
        if (status === 404) {
          error.value = 'Không tìm thấy'
        }
      } else {
        error.value = 'error'
      }
    } finally {
      loading.value = false
    }
  } else {
    error.value = result.error.issues.map((i) => i.message).join('\n,')
  }
}
</script>
<template>
  <form class="flex flex-col gap-4" @submit.prevent.stop="submit">
    <Label>Code của sản phẩm:</Label>
    <div class="max-w-96 flex gap-4 items-center">
      <Input
        type="text"
        placeholder="ABC00123"
        v-model="productCode"
        @input="(error = undefined)"
      />
      <Button type="button" @click="submit" variant="secondary" :disable="loading">
        Thêm
        <Icon v-if="loading" icon="lucide:loader-circle" class="animate-spin" />
      </Button>
    </div>
    <p class="text-sm text-muted-foreground -mt-3">Nhập vào sản phẩm sẽ thêm vào trong nhóm</p>
    <p class="text-[0.8rem] font-medium text-destructive -mt-3" v-if="error">{{ error }}</p>
  </form>
</template>
