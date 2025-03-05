<template>
  <Table>
    <TableBody>
      <TableRow>
        <TableHead>Phương thức thanh toán</TableHead>
        <TableCell>{{
          order.payment.method === PaymentMethod.CARD ?
            'Chuyển khoản' :
            order.payment.method === PaymentMethod.CASH
              ? 'Tiền mặt' : 'COD'
        }}</TableCell>
      </TableRow>
      <TableRow>
        <TableHead>Số tiền đã trả</TableHead>
        <TableCell>{{ formartCurrency(order.payment.amountPaid) }}</TableCell>
      </TableRow>
      <TableRow>
        <TableHead>Chi phí giao hàng</TableHead>
        <TableCell>{{ formartCurrency(order.payment.shippingPrice) }}</TableCell>
      </TableRow>
      <TableRow>
        <TableHead>Giá tiền các mặt hàng sơ bộ</TableHead>
        <TableCell>{{ formartCurrency(order.payment.subTotalPrice) }}</TableCell>
      </TableRow>
      <TableRow>
        <TableHead>Giá tiến cuối cùng</TableHead>
        <TableCell>{{ formartCurrency(order.payment.totalPrice) }}</TableCell>
      </TableRow>
      <TableRow>
        <TableHead>Trạng thái giao dịch</TableHead>
        <TableCell>{{ order.payment.status }}</TableCell>
      </TableRow>
      <TableRow>
        <TableHead>Ngày thanh toán </TableHead>
        <TableCell>{{ order.payment.paymentDate ? formatDate(order.payment.paymentDate) : 'Chưa có thông tin' }}
        </TableCell>
      </TableRow>

      <TableRow>
        <TableHead>Ngày Tạo </TableHead>
        <TableCell>{{ formatDate(order.payment.createdAt) }}</TableCell>
      </TableRow>

      <TableRow>
        <TableHead>Ngày Sửa đổi </TableHead>
        <TableCell>{{ formatDate(order.payment.updatedAt) }}</TableCell>
      </TableRow>
    </TableBody>
  </Table>
</template>

<script setup lang="ts">
import { PaymentMethod, type Order } from '@/types/orders/resType';
import { formartCurrency, formatDate } from '@/utils';
defineProps<{ order: Order }>()
</script>

<style scoped></style>
