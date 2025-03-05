<script setup lang="ts">
import { toast } from '@/components/ui/toast';
import { OrderStage, PaymentStatus } from '@/types/orders/resType';
import { toTypedSchema } from '@vee-validate/zod';
import { Form } from 'vee-validate';
import { z } from 'zod';

const props = defineProps<{ order: Awaited<ReturnType<typeof useOrder>> }>();
const router = useRouter();

const stepIndex = ref(1);

const steps = [
  {
    step: 1,
    title: 'Giá ship',
    description: 'Cập nhật thông tin giá tiền ship',
  },
  {
    step: 2,
    title: 'Khách hàng thanh toán',
    description: 'Cập nhật thông tin thanh toán của khách hàng',
  },
  {
    step: 3,
    title: 'Thành công',
    description: 'Tiến thành chuyển đổi trạng thái',
  },
]

const infoDescs: string[] = [
  'Thông tin thanh toán',
  'Thông tin thanh toán',
  'Thông tin Thanh toán'
];

const schema = [
  z.object({//OrderItem
    shippingPrice: z.boolean().refine(() => {
      return props.order.order.value.payment.status !== PaymentStatus.PROCESSING
    }, { message: "Hãy cập nhật giá Shipping . Bắt buộc phải có giá trị 0 hoặc lớn hơn" }),
    shippingPriceInfo: z.boolean()
  }),
  z.object({ // Order Address
    acceptPrice: z.boolean(),
    paymentPaid: z.boolean().refine(() => props.order.order.value.payment.status === PaymentStatus.PAID, "Khách hàng chưa thanh toán xong")
  }),
  z.object({
    message: z.string(),
  })
]

const openUpdatePaymentUpdateDialog = ref<boolean>(false);
const openUpdatePaymenPaidDialog = ref<boolean>(false);

async function onSubmit(message: string) {
  const result = await props.order.updateProcess(OrderStage.PACKING, message);
  if (result) {
    router.replace({ name: '/orders/[id]/process/packing', params: { id: props.order.order.value.id } })
  }
}

async function onPaymentChange() {
  props.order.refresh();
  openUpdatePaymentUpdateDialog.value = false;

  toast({ description: "Cập nhật thông tin giá cuối cùng thành công" })
}

async function onPaymentPaidChange() {
  props.order.refresh();
  openUpdatePaymenPaidDialog.value = false;
  toast({ description: "Cập nhật thông tin thanh toán khách hàng thành công" })
}

</script>

<template>
  <div class="mt-5">
    <Form v-slot="{ meta, values, validate }" as="" keep-values
      :validation-schema="toTypedSchema(schema[stepIndex - 1])">
      <Stepper v-slot="{ isNextDisabled, isPrevDisabled, nextStep, prevStep }" v-model="stepIndex" class="block w-full">
        <form class="flex flex-col gap-6" @submit="(e) => {
          e.preventDefault()
          validate()

          if (stepIndex === steps.length && meta.valid) {
            onSubmit(values.message)
          }
        }">
          <AppFormGroup title="Tiến trình" desc="Tiến trình xử lý đơn hàng mới">
            <div class="flex w-full flex-start gap-2">
              <StepperItem v-for="step in steps" :key="step.step" v-slot="{ state }"
                class="relative flex w-full flex-col items-center justify-center" :step="step.step">
                <StepperSeparator v-if="step.step !== steps[steps.length - 1].step"
                  class="absolute left-[calc(50%+20px)] right-[calc(-50%+10px)] top-5 block h-0.5 shrink-0 rounded-full bg-muted group-data-[state=completed]:bg-primary" />

                <StepperTrigger as-child>
                  <Button :variant="state === 'completed' || state === 'active' ? 'default' : 'outline'" size="icon"
                    class="z-10 rounded-full shrink-0"
                    :class="[state === 'active' && 'ring-2 ring-ring ring-offset-2 ring-offset-background']"
                    :disabled="state !== 'completed' && !meta.valid">
                    <Icon icon="lucide:check" v-if="state === 'completed'" class="size-5" />
                    <Icon icon="lucide:circle" v-if="state === 'active'" />
                    <Icon icon="lucide:dot" v-if="state === 'inactive'" />
                  </Button>
                </StepperTrigger>

                <div class="mt-5 flex flex-col items-center text-center">
                  <StepperTitle :class="[state === 'active' && 'text-primary']"
                    class="text-sm font-semibold transition lg:text-base">
                    {{ step.title }}
                  </StepperTitle>
                  <StepperDescription :class="[state === 'active' && 'text-primary']"
                    class="sr-only text-xs text-muted-foreground transition md:not-sr-only lg:text-sm">
                    {{ step.description }}
                  </StepperDescription>
                </div>
              </StepperItem>
            </div>
          </AppFormGroup>

          <AppFormGroup title="Thông tin" :desc="infoDescs[stepIndex - 1]">
            <OrderProcessInfoPayment :order="order.order.value" />
          </AppFormGroup>

          <AppFormGroup title="Hành động" desc="Một số hành động có thể thực hiện trong tiến trình">
            <Dialog v-model:open="openUpdatePaymentUpdateDialog">
              <DialogTrigger as-child>
                <Button variant="outline">
                  Chỉnh sửa giá tiền
                </Button>
              </DialogTrigger>
              <OrderProcessPaymentUpdateDialogContent v-if="openUpdatePaymentUpdateDialog" :id="order.order.value.id"
                :payment="order.order.value.payment" @payment-change="onPaymentChange" />
            </Dialog>

            <Dialog v-model:open="openUpdatePaymenPaidDialog">
              <DialogTrigger as-child>
                <Button variant="outline">
                  Cập nhật thông tin Thanh toán
                </Button>
              </DialogTrigger>
              <OrderProcessPaymentPaidUpdateDialogContent v-if="openUpdatePaymenPaidDialog" :id="order.order.value.id"
                :payment="order.order.value.payment" @payment-paid-change="onPaymentPaidChange" />
            </Dialog>
          </AppFormGroup>

          <AppFormGroup title="Yêu cầu" desc="Hãy thực hiện các yêu cầu theo tiến trình">
            <div class="flex flex-col gap-4 mt-4">
              <OrderProcessPaymentFormConfirmPaymentShip v-if="stepIndex === 1" />
              <OrderProcessPaymentFormConfirmPaymentPaid v-if="stepIndex === 2" />
              <AppFormFieldTextArea label="Ghi chú về đơn hàng"
                desc="Hãy mô tả về đơn hàng trước khi kết thúc tiến trình hiện tại" v-if="stepIndex === steps.length"
                name="message" placeholder="Xác nhận đơn hàng không có vấn đề gì ..." />
            </div>
          </AppFormGroup>




          <div class="flex items-center justify-between">
            <Button :disabled="isPrevDisabled" variant="outline" size="sm" @click="prevStep()">
              Quay lại
            </Button>
            <div class="flex items-center gap-3">
              <Button v-if="steps.length !== stepIndex" :type="meta.valid ? 'button' : 'submit'"
                :disabled="isNextDisabled" size="sm" @click="meta.valid && nextStep()">
                Tiếp tục
              </Button>
              <Button v-if="steps.length === stepIndex" size="sm" type="submit">
                Submit
              </Button>
            </div>
          </div>
        </form>
      </Stepper>
    </Form>

    <div id="dialog-container"></div>
  </div>
</template>

<style scoped></style>
