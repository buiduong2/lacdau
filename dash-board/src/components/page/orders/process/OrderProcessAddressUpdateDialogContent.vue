<script setup lang="ts">
import type { Order } from '@/types/orders/resType';
import { toTypedSchema } from '@vee-validate/zod';
import { useForm } from 'vee-validate';
import { z } from 'zod';

const schema = z.object({
  fullName: z.string(),
  phone: z.string(),
  districtId: z.string(),
  provinceId: z.string(),
  detail: z.string(),
  message: z.string().transform(s => s.trim().length ? s : undefined).optional(),
  description: z.string(),
})
const props = defineProps<{ id: number, address?: Order['orderAddress'] }>();
const emits = defineEmits<{ (e: 'addressChange'): void }>();
const form = useForm({
  validationSchema: toTypedSchema(schema),
  initialValues: props.address ? {
    ...props.address,
    districtId: String(props.address.districtId),
    provinceId: String(props.address.provinceId)
  } : {}
});

const onSubmit = form.handleSubmit(async (values) => {
  await fetchOrderUpdateAddress(props.id, {
    description: values.description,
    payload: {
      detail: values.detail,
      districtId: Number(values.districtId),
      fullName: values.fullName,
      phone: values.phone,
      message: values.message
    }
  });

  emits('addressChange');
})

</script>

<template>


  <DialogScrollContent class="sm:max-w-[425px]" to="#dialog-container">
    <form class="flex gap-4 flex-col" @submit.prevent="onSubmit">
      <DialogHeader>
        <DialogTitle>Cập nhật thông tin giao hàng</DialogTitle>
        <DialogDescription>
          Hãy điền đầy đủ thông tin để có thể giao hàng thuận lợi
        </DialogDescription>
      </DialogHeader>
      <AppFormFieldInput name="fullName" label="Tên Người nhận" is-required type="text" placeholder="Nguyễn Văn A" />
      <AppFormFieldInput name="phone" label="Số điện thoại" type="text" placeholder="01234567890" is-required />
      <AppFormAddressDistrict district-id-name="districtId" province-id-name="provinceId"
        :province-id-value="form.values.provinceId" />
      <AppFormFieldTextArea name="detail" label="Địa chỉ chi tiết" placeholder="Số 123. Đường 456 Quận 7 TP 8" />
      <AppFormFieldTextArea name="message" label="Ghi chú khách hàng"
        placeholder="Đến nơi thì gọi điện xuống nhận..." />
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
