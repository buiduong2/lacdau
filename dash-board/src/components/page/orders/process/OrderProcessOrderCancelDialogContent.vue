<script setup lang="ts">
import { OrderStage } from '@/types/orders/resType';
import { toTypedSchema } from '@vee-validate/zod';
import { useForm } from 'vee-validate';
import { z } from 'zod';

const props = defineProps<{ id: number }>();
const emits = defineEmits<{ (e: 'stageChange'): void }>();
const schema = z.object({
  description: z.string().min(1)
})

const form = useForm({
  validationSchema: toTypedSchema(schema)
})

const onSubmit = form.handleSubmit(async (values) => {
  await fetchOrderProcess(props.id, OrderStage.FAILURE, values.description);
  emits('stageChange')
})
</script>
<template>
  <DialogScrollContent class="sm:max-w-[425px]">
    <form class="flex gap-4 flex-col" @submit.prevent="onSubmit">
      <DialogHeader>
        <DialogTitle class="text-destructive">Hủy đơn hàng</DialogTitle>
        <DialogDescription>
          Chú ý khi hủy đơn hàng ko thể quay trở lại
        </DialogDescription>
      </DialogHeader>
      <AppFormFieldTextArea name="description" label="Ghi chú nhân viên" placeholder="Sửa lại vì lý do... "
        is-required />
      <DialogFooter>
        <Button type="submit" variant="destructive">
          Hủy đơn hàng
        </Button>
      </DialogFooter>
    </form>
  </DialogScrollContent>
</template>


<style scoped></style>
