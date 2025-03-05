<script setup lang="ts">
import type { Order } from '@/types/orders/resType';

import { formartCurrency, formatDate } from '@/utils';

const props = defineProps<{ order: Order }>()
</script>

<template>
  <Table v-if="order.shipment">
    <TableBody>
      <TableRow>
        <TableHead>Thời gian giao hàng dự kiến</TableHead>
        <TableCell>{{ formatDate(order.shipment.estimatedDelivery) }}</TableCell>
      </TableRow>
      <TableRow>
        <TableHead>Chi phí giao hàng</TableHead>
        <TableCell>{{ formartCurrency(order.shipment.shippingPrice) }}</TableCell>
      </TableRow>
      <TableRow>
        <TableHead>Thông tin theo dõi giao hàng</TableHead>
        <TableCell>{{ order.shipment.trackingDetail }}</TableCell>
      </TableRow>
      <TableRow>
        <TableHead>Người vận chuyển: </TableHead>
        <TableCell>{{ order.shipment.carrier }}</TableCell>
      </TableRow>

      <TableRow>
        <TableHead>Ngày Tạo </TableHead>
        <TableCell>{{ formatDate(order.shipment.createdAt) }}</TableCell>
      </TableRow>

      <TableRow>
        <TableHead>Ngày Sửa đổi </TableHead>
        <TableCell>{{ formatDate(order.shipment.updatedAt) }}</TableCell>
      </TableRow>
    </TableBody>
  </Table>
  <p v-else class="text-center italic">Chưa có thông tin vận chuyển</p>
</template>


<style scoped></style>
