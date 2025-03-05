<script setup lang="ts">
import { PaymentMethod, type Order } from '@/types/orders/resType';
import { toTypedSchema } from '@vee-validate/zod';
import { AxiosError } from 'axios';
import { useForm } from 'vee-validate';
import { z } from 'zod';
import { addErrorFromServer } from '../../app/form/utils';

const props = defineProps<{ id: number, payment: Order['payment'] }>();
const emits = defineEmits<{ (e: 'paymentChange'): void }>();
const methodValues: string[] = Object.values(PaymentMethod);
if (methodValues.length == 0) {
  throw new Error("PaymentMethod should have an enum")
}
const methodEnums = methodValues as [string, ...string[]];
const methodSelectValues: { value: PaymentMethod, label: string }[] = [
  {
    label: "Chuyển khoản",
    value: PaymentMethod.CARD
  },
  {
    label: "COD",
    value: PaymentMethod.COD
  },
  {
    label: "Tiền mặt",
    value: PaymentMethod.CASH
  }
]
const schema = z.object({
  description: z.string().min(1),
  payload: z.object({
    method: z.enum(methodEnums),
    shippingPrice: z.number().gte(0)
  })
})

const form = useForm({
  validationSchema: toTypedSchema(schema),
  initialValues: {
    payload: {
      method: props.payment.method
    }
  }
})

const onSubmit = form.handleSubmit(async (values) => {
  try {
    await fetchOrderUpdatePayment(props.id, values);
    emits('paymentChange')
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
        <DialogTitle>Cập nhật thông tin giá tiền</DialogTitle>
        <DialogDescription>
          Hãy cập nhật giá Ship để hoàn thành tiến trình tính toán Payment

        </DialogDescription>
      </DialogHeader>
      <AppFormFieldSelect name="payload.method" label="Phương thức thanh toán" is-required placeholder="Chưa có"
        :values="methodSelectValues" />
      <AppFormFieldInput name="payload.shippingPrice" label="Giá vận chuyển" is-required placeholder="10000"
        type="number" />
      <AppFormFieldTextArea name="description" label="Ghi chú nhân viên" placeholder="Sửa lại vì lý do... "
        is-required />
      <DialogFooter>
        <Button type="submit">
          Save changes
        </Button>
      </DialogFooter>
    </form>
  </DialogScrollContent>
</template>


<style scoped></style>
