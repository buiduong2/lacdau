<template>
  <Table class="min-w-max">
    <TableHeader>
      <TableRow>
        <TableHead>Mã SP</TableHead>
        <TableHead class="w-[100px]">
        </TableHead>
        <TableHead class="w-52">Tên SP</TableHead>
        <TableHead class="text-center">
          SL Yêu cầu
        </TableHead>
        <TableHead class="text-center">
          SL cung cấp
        </TableHead>
        <TableHead>
          TT cung cấp
        </TableHead>
        <TableHead class="text-center">
          Đơn giá
        </TableHead>
        <TableHead class="text-center">
          Giá khuyến mại
        </TableHead>

      </TableRow>
    </TableHeader>
    <TableBody>
      <TableRow v-for="item in order.orderItems" :key="item.id">
        <TableCell class="font-medium">
          {{ item.product.id }}
        </TableCell>
        <TableCell>
          <img class="size-20 rounded-lg" :src="item.product.mainImage?.src || '/placeholder.svg'" />
        </TableCell>
        <TableCell>{{ item.product.name }}</TableCell>
        <TableCell class="text-right">
          {{ item.requestedQuantity }}
        </TableCell>
        <TableCell class="text-right">
          {{ item.suppliedQuantity }}
        </TableCell>
        <TableCell>
          <Badge v-if="item.status === OrderItemStatus.FULLY_SUPPLIED" variant="outline">Đầy đủ</Badge>
          <Badge v-else-if="item.status === OrderItemStatus.PARTIALLY_SUPPLIED" variant="default">Bị thiếu
          </Badge>
          <Badge v-else-if="item.status === OrderItemStatus.NOT_SUPPLIED" variant="destructive">Hét hàng
          </Badge>
        </TableCell>

        <TableCell>
          {{ formartCurrency(item.unitPrice) }}
        </TableCell>

        <TableCell>
          {{ formartCurrency(item.salePrice || 0) }}
        </TableCell>
      </TableRow>
    </TableBody>

  </Table>
</template>

<script setup lang="ts">
import { OrderItemStatus, type Order } from '@/types/orders/resType';
import { formartCurrency } from '@/utils';
defineProps<{ order: Order }>()
</script>

<style scoped></style>
