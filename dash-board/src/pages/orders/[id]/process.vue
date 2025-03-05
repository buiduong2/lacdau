<script setup lang="ts">
import OrderFormMainProcess from '@/components/page/orders/form/OrderFormMainProcess.vue';
import { OrderStage } from '@/types/orders/resType';

definePage({
  meta: { breadcrumb: 'Xử lý đơn hàng' },
})
const router = useRouter();
const route = useRoute('/orders/[id]/process')
const orderId = route.params.id;
let order: Awaited<ReturnType<typeof useOrder>>;

try {
  order = await useOrder(orderId);
  router.replace({
    name: '/orders/[id]/process/' + order.order.value.stage.toLowerCase() as any,
    params: { id: orderId }
  });
} catch (error) {
  router.push('/404')
}

const openCancelDialog = ref(false);

const doneStages: OrderStage[] = [OrderStage.COMPLETED];
const cancelStages: OrderStage[] = [OrderStage.FAILURE, OrderStage.CANCELED];

const isCancelOrderAble = computed<boolean>(() => {
  const currentStage = order.order.value.stage;
  if (doneStages.includes(currentStage)) {
    return false;
  }
  if (cancelStages.includes(currentStage)) {
    return false;
  }

  return true;
})

async function onOrderCancel() {
  await order.refresh();
  openCancelDialog.value = false;
  router.push({ name: '/orders/[id]/process/failure', params: { id: orderId } })
}

</script>
<template>


  <template v-if="order">
    <OrderFormProcessLayout title="Xử lý đơn hàng">

      <Suspense>
        <RouterView :order="order"></RouterView>
      </Suspense>

      <template #buttons>
        <Dialog v-if="isCancelOrderAble" v-model:open="openCancelDialog">
          <DialogTrigger as-child>
            <Button variant="destructive">
              Hủy đơn hàng
            </Button>
          </DialogTrigger>
          <OrderProcessOrderCancelDialogContent v-if="openCancelDialog" :id="order.order.value.id"
            @stage-change="onOrderCancel" />
        </Dialog>
        <Button>Xem Thông tin đầy đủ</Button>
      </template>
      <template #right>
        <OrderFormMainProcess :order="order.order.value" />
      </template>
    </OrderFormProcessLayout>
  </template>

</template>


<style scoped></style>
