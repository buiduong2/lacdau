<script setup lang="ts">
import type { Order } from '@/types/orders/resType';
import { toTypedSchema } from '@vee-validate/zod';
import { useForm } from 'vee-validate';
import { z } from 'zod';
import { addErrorFromServer } from '../../app/form/utils';
import { AxiosError } from 'axios';

const props = defineProps<{ id: number, shipment: Order['shipment'] }>();
const emits = defineEmits<{ (e: 'shipmentChange'): void }>()
const schema = z.object({
  description: z.string().min(1),
  payload: z.object({
    estimatedDelivery: z.date().min(new Date()),
    shippingPrice: z.number().gte(0),
    trackingDetail: z.string().min(1),
    carrier: z.string().min(1),
  })
});

const form = useForm({
  validationSchema: toTypedSchema(schema),
  initialValues: props.shipment ? {
    payload: {
      estimatedDelivery: new Date(props.shipment.estimatedDelivery),
      shippingPrice: props.shipment.shippingPrice,
      trackingDetail: props.shipment.trackingDetail,
      carrier: props.shipment.carrier
    }
  } : {
    payload: {
      estimatedDelivery: new Date()
    }
  }
})


const onSubmit = form.handleSubmit(async (values) => {

  try {
    await fetchOrderUpdateShipment(props.id, values);
    emits('shipmentChange')
  } catch (error) {
    if (error instanceof AxiosError && error.status === 400) {
      addErrorFromServer(form, error.response?.data)
    }
  }
})



</script>
<template>
  <DialogScrollContent class="sm:max-w-[425px]" to="#dialog-container">
    <form class="flex gap-4 flex-col" @submit.prevent="onSubmit">
      <DialogHeader>
        <DialogTitle>Thêm/Cập nhật thông tin giao hàng</DialogTitle>
        <DialogDescription>
          Hãy thêm các thông tin về giao hàng sau khi bạn làm việc với nhà cung cấp giao hàng
        </DialogDescription>
      </DialogHeader>
      <AppFormDateTimePicker name="payload.estimatedDelivery" label="Thời gian nhận hàng dự kiện" is-required />
      <AppFormFieldInput name="payload.shippingPrice" label="Giá giao hàng thực tế" type="number" is-required
        placeholder="10000" />
      <AppFormFieldTextArea name="payload.trackingDetail" label="Thông tin theo dõi" is-required
        placeholder="Đơn vị giao hàng. Grap . Mã chuyến hàng: VD1234" />
      <AppFormFieldInput name="payload.carrier" label="Tên người vận chuyển" placeholder="Nguyễn Văn A" is-required
        type="text" />
      <AppFormFieldTextArea name="description" label="Ghi chú của nhân viên" is-required
        placeholder="Khách hàng yêu cầu" />
      <DialogFooter>
        <Button type="submit">
          Save changes
        </Button>
      </DialogFooter>
    </form>
  </DialogScrollContent>
</template>



<style scoped></style>
