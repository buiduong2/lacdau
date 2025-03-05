<script setup lang="ts">
import { type Order } from '@/types/orders/resType';
import { toTypedSchema } from '@vee-validate/zod';
import { AxiosError } from 'axios';
import { useForm } from 'vee-validate';
import { z } from 'zod';
import { addErrorFromServer } from '../../app/form/utils';

const props = defineProps<{ id: number, payment: Order['payment'], refund?: boolean }>();
const emits = defineEmits<{ (e: 'paymentPaidChange'): void }>();
const schema = z.object({
  description: z.string().min(1),
  payload: z.object({
    amountPaid: z.number().refine(n => n !== 0 || props.payment.totalPrice === 0, 'Đơn hàng không miễn phí. Vùi lòng nhập lại'),
  })
})

const form = useForm({
  validationSchema: toTypedSchema(schema),
})

const onSubmit = form.handleSubmit(async (values) => {
  try {
    if (props.refund) {
      await fetchOrderUpdatePaymentRefund(props.id, values);
    } else {
      await fetchOrderUpdatePaymentPaid(props.id, values);
    }
    emits('paymentPaidChange')
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
          Hãy làm việc với khách hàng rồi nhập thông tin thanh toán bên dưới:
        </DialogDescription>
      </DialogHeader>

      <AppFormFieldInput name="payload.amountPaid" label="Số lượng tiền đã thanh toán" is-required placeholder="10000"
        desc="Nhập một âm để biểu đạt đã bồi hoàn thành công" type="number" />
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
