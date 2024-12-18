<script setup lang="ts">
import type { ProductRelateInfo } from '@/types/relate-groups/reqTypes'

defineProps<{
  products: ProductRelateInfo[]
  fieldName: string
  groupId?: number
}>()

defineEmits<{
  (e: 'removeProduct', index: number): void
}>()
</script>
<template>
  <TooltipProvider :delayDuration="0" v-if="products.length">
    <Card v-for="(product, index) in products">
      <CardHeader class="flex flex-row justify-start gap-2">
        <CardTitle class="">
          {{ product.name }}
        </CardTitle>
        <div class="flex-shrink-0 flex gap-2">
          <Tooltip>
            <TooltipTrigger as-child>
              <Button type="button" size="icon" variant="outline">
                <Icon
                  v-if="!product.relateInfo?.relateGroupId"
                  icon="lucide:circle-alert"
                  class="size-4 text-destructive"
                />
                <Icon
                  v-else-if="product.relateInfo.relateGroupId === groupId"
                  class="size-4 text-green-500"
                  icon="lucide:shield-check"
                />
                <Icon v-else icon="lucide:circle-check" class="size-4 text-green-500" />
              </Button>
            </TooltipTrigger>
            <TooltipContent>
              <p v-if="!product.relateInfo?.relateGroupId">
                Đã tồn tại trong nhóm khác. <br />
                Sẽ ghi đè thông tin cũ
              </p>
              <p v-else-if="product.relateInfo.relateGroupId === groupId">
                Sản phẩm thuộc nhóm này ngay từ ban đầu
              </p>
              <p v-else>Sản phẩm mới sẵn sàng</p>
            </TooltipContent>
          </Tooltip>

          <DropdownMenu>
            <DropdownMenuTrigger as-child>
              <Button type="button" size="icon" variant="outline">
                <Icon icon="lucide:settings-2" class="size-4" />
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent>
              <DropdownMenuItem @click="$emit('removeProduct', index)"
                ><Icon icon="lucide:trash" />Xóa</DropdownMenuItem
              >
              <DropdownMenuItem v-if="index >= 1">
                <Icon icon="lucide:move-left" />Sang trái</DropdownMenuItem
              >
              <DropdownMenuItem v-if="index < products.length - 1">
                <Icon icon="lucide:move-right" />Sang phải</DropdownMenuItem
              >
            </DropdownMenuContent>
          </DropdownMenu>
        </div>
      </CardHeader>
      <CardContent class="flex flex-col gap-1">
        <p>ID: {{ product.id }}</p>
        <p>Giá: {{ product.originalPrice }}</p>
        <p>Giá Khuyến mại: {{ product.salePrice || 'Chưa có' }}</p>
        <p class="flex gap-2 items-center">
          <AppFormFieldInput
            :name="`${fieldName}[${index}].name`"
            is-required
            label="Tên liên kết"
            placeholder="Màu A"
            type="text"
          />
        </p>
      </CardContent>
    </Card>
  </TooltipProvider>
  <h3 v-else class="italic text-center">Chưa có sản phẩm liên kết nào</h3>
</template>
