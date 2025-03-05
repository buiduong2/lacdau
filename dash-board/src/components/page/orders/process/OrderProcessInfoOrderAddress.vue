<template>
  <Table v-if="order.orderAddress">
    <TableBody>
      <TableRow>
        <TableHead>Tên khách hàng</TableHead>
        <TableCell>{{ order.orderAddress.fullName }}</TableCell>
      </TableRow>
      <TableRow>
        <TableHead>Số điện thoại</TableHead>
        <TableCell>{{ order.orderAddress.phone }}</TableCell>
      </TableRow>
      <TableRow>
        <TableHead>Tỉnh / Thành phố</TableHead>
        <TableCell>{{ order.orderAddress.province }}</TableCell>
      </TableRow>
      <TableRow>
        <TableHead>Quận / Huyện</TableHead>
        <TableCell>{{ order.orderAddress.district }}</TableCell>
      </TableRow>
      <TableRow>
        <TableHead>Địa chỉ chi tiết</TableHead>
        <TableCell>{{ order.orderAddress.detail }}</TableCell>
      </TableRow>
      <TableRow>
        <TableHead>Tin nhắn: </TableHead>
        <TableCell>{{ order.orderAddress.message }}</TableCell>
      </TableRow>
    </TableBody>
  </Table>
</template>

<script setup lang="ts">
import type { Order } from '@/types/orders/resType';

defineProps<{ order: Order }>();
</script>

<style scoped></style>
