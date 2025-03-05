<script setup lang="ts">
const route = useRoute('/orders/[id]/');

const order = await useOrder(route.params.id);
</script>

<template>
  <AppDetailLayout :title="'Thông tin chi tiết đơn hàng: #' + order.order.value.id">
    <template #actions>
      <Button @click="$router.push({ name: '/orders/[id]/history', params: { id: route.params.id } })">
        Xem thông tin lịch sử
      </Button>
      <Button @click="$router.push({ name: '/orders/[id]/process', params: { id: route.params.id } })">
        Xem tiến trình</Button>
    </template>
    <template #left>
      <Card class="w-full overflow-auto">
        <CardHeader>
          <CardTitle>Thông tin Đơn hàng: </CardTitle>
        </CardHeader>
        <CardContent>
          <OrderProcessInfoOrderItem :order="order.order.value" />
        </CardContent>
      </Card>
      <Card>
        <CardHeader>
          <CardTitle>Thông tin Khách hàng: </CardTitle>
        </CardHeader>
        <CardContent>
          <OrderProcessInfoCustomer :order="order.order.value" />
        </CardContent>
        <CardFooter class="items-center justify-center">
          <Button @click="$router.push({ name: '/customers/[id]', params: { id: order.order.value.customer.id } })">
            Xem thông tin khách hàng
          </Button>
        </CardFooter>
      </Card>
      <div class="grid grid-cols-2 gap-8">
        <Card>
          <CardHeader>
            <CardTitle>Thông tin người nhận: </CardTitle>
          </CardHeader>
          <CardContent>
            <OrderProcessInfoOrderAddress :order="order.order.value" />
          </CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle>Thông tin giao hàng: </CardTitle>
          </CardHeader>
          <CardContent>
            <OrderProcessInfoShipment :order="order.order.value" />
          </CardContent>
        </Card>
      </div>
    </template>
    <template #right>
      <Card>
        <CardHeader>
          <CardTitle>Thông tin thanh toán: </CardTitle>
        </CardHeader>
        <CardContent>
          <OrderProcessInfoPayment :order="order.order.value" />
        </CardContent>
      </Card>
      <Card>
        <CardHeader>
          <CardTitle>Trạng thái: </CardTitle>
        </CardHeader>
        <CardContent class="flex flex-col gap-2">
          <Table>
            <TableRow>
              <TableHead>Thanh toán</TableHead>
              <TableCell>
                <b class="text-base">{{ order.order.value.payment.status }}</b>
              </TableCell>
            </TableRow>
            <TableRow>
              <TableHead>
                Giao đoạn
              </TableHead>
              <TableCell>
                <b class="text-base">{{ order.order.value.stage }}</b>
              </TableCell>
            </TableRow>
          </Table>
        </CardContent>
      </Card>
    </template>
  </AppDetailLayout>
</template>


<style scoped></style>
