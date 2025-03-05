<template>
  <Table v-if="order.orderAddress">
    <TableBody>
      <TableRow>
        <TableHead>ID</TableHead>
        <TableCell>{{ order.customer.id }}</TableCell>
      </TableRow>
      <TableRow>
        <TableHead>Tên khách hàng</TableHead>
        <TableCell>{{ order.customer.firstName }}</TableCell>
      </TableRow>
      <TableRow>
        <TableHead>Họ Khách hàng</TableHead>
        <TableCell>{{ order.customer.lastName }}</TableCell>
      </TableRow>
      <TableRow>
        <TableHead>Giới tính</TableHead>
        <TableCell>{{ order.customer.gender === 'MALE' ? 'Nam' : 'Nữ' }}</TableCell>
      </TableRow>
      <TableRow>
        <TableHead>Địa chỉ Email</TableHead>
        <TableCell>{{ order.customer.email }}</TableCell>
      </TableRow>
      <TableRow>
        <TableHead>Số điện thoại </TableHead>
        <TableCell>{{ order.customer.phone }}</TableCell>
      </TableRow>

      <TableRow>
        <TableHead>Ngày tạo </TableHead>
        <TableCell>{{ formatDate(order.customer.createdAt) }}</TableCell>
      </TableRow>
    </TableBody>
  </Table>
</template>

<script setup lang="ts">
import { type Order } from '@/types/orders/resType';
import { formatDate } from '@/utils';
defineProps<{ order: Order }>()
</script>

<style scoped></style>
