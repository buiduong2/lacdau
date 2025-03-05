<script setup lang="ts">
import { OrderStage, OrderStageAction, type OrderLog } from '@/types/orders/resType';
import { AxiosError } from 'axios';
import { formatDate } from '@/utils';
const route = useRoute('/orders/[id]/history');
const router = useRouter()
let orderLogs: OrderLog[] = [];
try {
  orderLogs = (await fetchOrderLogs(route.params.id)).data
} catch (error) {
  if (error instanceof AxiosError && error.status === 404) {
    router.push('/404');
  }
};

function getActionDesc(action: OrderStageAction): string {
  switch (action) {
    case OrderStageAction.CREATE_SHIPMENT:
      return 'Tạo mới <b>giao hàng</b>'
    case OrderStageAction.UPDATE_ORDER_ITEM:
      return "Cập nhật <b>đơn hàng</b>"
    case OrderStageAction.UPDATE_ADDRESS:
      return "Cập nhật <b>địa chỉ</b>"
    case OrderStageAction.UPDATE_PAYMENT:
      return "Cập nhật thông tin <b>thanh toán</b>"
    case OrderStageAction.UPDATE_PAYMENT_AMOUNT_PAID:
      return "Cập nhật thông tin <b>thanh toán khách hàng</b>"

    case OrderStageAction.UPDATE_PAYMENT_REFUND:
      return "Cập nhật thông tin <b>bồi hoàn</b>"
    case OrderStageAction.UPDATE_SHIPMENT:
      return "Cập nhật thông tin <b>giao hàng</b>"
    default:
      break;
  }
  throw new Error("Action Description not implmeneted");
}

const cancelStages: OrderStage[] = [OrderStage.CANCELED, OrderStage.FAILURE];

</script>
<template>
  <AppFormLayout title="Lịch sử tiến trình đơn hàng">
    <Card>
      <CardHeader>
        <CardTitle class="text-lg">Tiến trình đơn hàng</CardTitle>
        <CardDescription>
          Thông tin về tiến trình được hiển thị ở đây.
          <br>
          Bao gồm các lịch sử thay đổi thông tin cũng như thay đổi các trạng thái của đơn hàng
        </CardDescription>
      </CardHeader>
      <CardContent class="flex flex-col gap-3">
        <Card v-for="log in orderLogs" :key="log.id" class="rounded-md" :class="log.action ? 'border border-blue-300 ml-10' :
          cancelStages.includes(log.stage) ? 'border border-red-300' : 'border border-green-300'
          ">

          <CardHeader class="pb-3">
            <template v-if="log.action">
              <CardTitle>SỬA ĐỔI</CardTitle>
              <CardDescription><span v-html="getActionDesc(log.action)"></span></CardDescription>
            </template>
            <template v-else>
              <CardTitle>THÀNH CÔNG</CardTitle>
              <CardDescription>Trạng thái <b>{{ log.stage }}</b> chuyển đổi thành công</CardDescription>
            </template>
          </CardHeader>
          <CardContent class="pb-3">
            <p class="text-sm">Ghi chú: {{ log.description }}</p>
          </CardContent>
          <CardFooter class="text-muted-foreground text-sm italic">Vào lúc: &nbsp; <b>{{ formatDate(log.createdAt)
              }}</b>
          </CardFooter>
        </Card>
      </CardContent>
      <CardFooter class="justify-center">
        <Button @click="$router.push({ name: '/orders/[id]/', params: { id: route.params.id } })">Xem thông tin chi
          tiết
          đơn
          hàng</Button>
      </CardFooter>
    </Card>

  </AppFormLayout>
</template>



<style scoped></style>
